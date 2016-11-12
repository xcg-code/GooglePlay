package com.app.googleplay.manager;

import android.content.Intent;
import android.net.Uri;

import com.app.googleplay.domain.AppInfo;
import com.app.googleplay.domain.DownloadInfo;
import com.app.googleplay.http.HttpHelper;
import com.app.googleplay.utils.IOUtils;
import com.app.googleplay.utils.UIUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 下载管理器
 * <p>
 * - 未下载 - 等待下载 - 正在下载 - 暂停下载 - 下载失败 - 下载成功
 * <p>
 * DownloadManager: 被观察者, 有责任通知所有观察者状态和进度发生变化
 * Created by 14501_000 on 2016/11/6.
 */

public class DownloadManager {
    public static final int STATE_UNDO = 1;
    public static final int STATE_WAITING = 2;
    public static final int STATE_DOWNLOADING = 3;
    public static final int STATE_PAUSE = 4;
    public static final int STATE_ERROR = 5;
    public static final int STATE_SUCCESS = 6;

    private static DownloadManager mDM = new DownloadManager();

    private DownloadManager() {

    }

    public static DownloadManager getInstance() {
        if (mDM == null) {
            mDM = new DownloadManager();
        }
        return mDM;
    }

    /**
     * 1.声明观察者接口
     */
    public interface DownloadObserve {
        //下载状态变化
        void onDownloadStateChange(DownloadInfo info);

        //下载进度变化
        void onDownloadProgressChange(DownloadInfo info);

    }

    /**
     * 2.观察者集合
     */
    ArrayList<DownloadObserve> observes = new ArrayList<DownloadObserve>();

    /**
     * 3.注册观察者
     */
    public synchronized void registerObserve(DownloadObserve observe) {
        if (observe != null && !observes.contains(observe)) {
            observes.add(observe);
        }

    }

    /**
     * 4.取消观察者
     */
    public synchronized void unregisterObserve(DownloadObserve observe) {
        if (observe != null && observes.contains(observe)) {
            observes.remove(observe);
        }
    }

    /**
     * 5.通知下载状态变化
     */
    public synchronized void notifyDownloadStateChange(DownloadInfo info) {
        for (DownloadObserve observe : observes) {
            observe.onDownloadStateChange(info);
        }
    }

    /**
     * 6.通知下载进度变化
     */
    public synchronized void notifyDownloadProgressChange(DownloadInfo info) {
        for (DownloadObserve observe : observes) {
            observe.onDownloadProgressChange(info);
        }
    }

    // 下载对象的集合, 使用线程安全的HashMap
    private ConcurrentHashMap<String, DownloadInfo> mDownloadInfoMap = new ConcurrentHashMap<String, DownloadInfo>();
    // 下载任务的集合
    private ConcurrentHashMap<String, DownloadTask> mDownloadTaskMap = new ConcurrentHashMap<String, DownloadTask>();

    //开始下载
    public synchronized void download(AppInfo info) {
        //判断应用是否是第一次下载，如果是则创建新的DownloadInfo对象，从头下载
        //如果不是，则接着下载，实现断点续传
        DownloadInfo downloadInfo = mDownloadInfoMap.get(info.id);
        if (downloadInfo == null) {
            downloadInfo = DownloadInfo.copy(info);//生成一个下载的对象
        }
        downloadInfo.currentState = STATE_WAITING;//状态为等待下载
        notifyDownloadStateChange(downloadInfo);
        System.out.println(downloadInfo.name + "等待下载啦");
        //将下载对象加入集合
        mDownloadInfoMap.put(downloadInfo.id, downloadInfo);

        // 初始化下载任务, 并放入线程池中运行
        DownloadTask downloadTask = new DownloadTask(downloadInfo);
        ThreadManager.getThreadPool().execute(downloadTask);
        //将任务放入集合
        mDownloadTaskMap.put(downloadInfo.id, downloadTask);


    }

    class DownloadTask implements Runnable {
        private DownloadInfo downloadInfo;

        public DownloadTask(DownloadInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
        }

        @Override
        public void run() {
            System.out.println(downloadInfo.name + "开始下载啦");
            downloadInfo.currentState = STATE_DOWNLOADING;
            notifyDownloadStateChange(downloadInfo);
            File file = new File(downloadInfo.path);
            HttpHelper.HttpResult httpResult;
            InputStream in = null;
            // 从头开始下载
            if (!file.exists() || file.length() != downloadInfo.currentPos || downloadInfo.currentPos == 0) {
                //删除无效文件
                file.delete();
                downloadInfo.currentPos = 0;

                //从头开始下载
                httpResult = HttpHelper.download(HttpHelper.URL + downloadInfo.downloadUrl);
                in = httpResult.getInputStream();
            } else {
                //断点续传
                // range 表示请求服务器从文件的哪个位置开始返回数据
               // httpResult = HttpHelper.download(HttpHelper.URL + downloadInfo.downloadUrl + "&range=" + file.length());
                URL url = null;
                try {
                    url = new URL(HttpHelper.URL + downloadInfo.downloadUrl);
                    HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
                    // 设置 User-Agent
                    httpConnection.setRequestProperty("User-Agent", "NetFox");
                    // 设置断点续传的开始位置
                    httpConnection.setRequestProperty("RANGE", "bytes="+file.length());
                    // 获得输入流
                    in = httpConnection.getInputStream();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in!= null) {
                FileOutputStream fos = null;
                try {
                    //在原文基础上追加数据
                    fos = new FileOutputStream(file, true);
                    int len;
                    byte[] buffer = new byte[1024];
                    // 只有状态是正在下载, 才继续轮询. 解决下载过程中中途暂停的问题
                    while ((len = in.read(buffer)) != -1
                            && downloadInfo.currentState == STATE_DOWNLOADING) {
                        fos.write(buffer, 0, len);
                        fos.flush();//把剩余数据刷新进本地

                        //更新下载进度
                        downloadInfo.currentPos += len;
                        notifyDownloadProgressChange(downloadInfo);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.close(in);
                    IOUtils.close(fos);
                }
                //文件下载结束
                if (file.length() == downloadInfo.size) {
                    //文件完整，下载成功
                    downloadInfo.currentState = STATE_SUCCESS;
                    notifyDownloadStateChange(downloadInfo);
                } else if (downloadInfo.currentState == STATE_PAUSE) {
                    //中途暂停
                    notifyDownloadStateChange(downloadInfo);
                } else {
                    //下载失败
                    file.delete();//删除无效文件
                    downloadInfo.currentState = STATE_ERROR;
                    downloadInfo.currentPos = 0;
                    notifyDownloadStateChange(downloadInfo);
                }
            } else {
                //网络异常
                System.out.println("网络异常");
                file.delete();
                downloadInfo.currentState = STATE_ERROR;
                downloadInfo.currentPos = 0;
                notifyDownloadStateChange(downloadInfo);
            }
            //从集合中移除下载任务
            mDownloadTaskMap.remove(downloadInfo.id);
        }
    }

    //下载暂停
    public synchronized void pause(AppInfo info) {
        //取出正在下载的对象
        DownloadInfo downloadInfo = mDownloadInfoMap.get(info.id);
        if (downloadInfo != null) {
            //对象处于下载或等待状态才暂停
            if (downloadInfo.currentState == STATE_DOWNLOADING
                    || downloadInfo.currentState == STATE_WAITING) {
                DownloadTask task = mDownloadTaskMap.get(downloadInfo.id);
                if (task != null) {
                    // 移除下载任务, 如果任务还没开始,正在等待, 可以通过此方法移除
                    // 如果任务已经开始运行, 需要在run方法里面进行中断
                    ThreadManager.getThreadPool().cancel(task);
                }
                //切换状态
                downloadInfo.currentState = STATE_PAUSE;
                notifyDownloadStateChange(downloadInfo);
            }
        }

    }

    //应用安装
    public synchronized void install(AppInfo info) {
        DownloadInfo downloadInfo = mDownloadInfoMap.get(info.id);
        if (downloadInfo != null) {
            // 跳到系统的安装页面进行安装
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + downloadInfo.path),
                    "application/vnd.android.package-archive");
            UIUtils.getContext().startActivity(intent);
        }
    }

    public DownloadInfo getDownloadInfo(AppInfo info) {
        return mDownloadInfoMap.get(info.id);
    }

}
