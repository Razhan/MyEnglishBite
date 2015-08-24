package com.ef.bite.business;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.ef.bite.AppConst;
import com.ef.bite.R;
import com.ef.bite.utils.FileUtils;

//下载新版本并在通知栏显示
public class VersionUpdateBLL extends Service {

    private static final int TIMEOUT = 10 * 1000;
    private static String down_url;
    private static final int DOWN_OK = 1;
    private static final int DOWN_ERROR = 0;
    private static final int down_step_custom = 5;

    private String app_name;

    private NotificationManager notificationManager;
    private Notification notification;
    private Intent updateIntent;
    private PendingIntent pendingIntent;
    private RemoteViews contentView;

    private File updateFile;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        app_name = intent.getStringExtra("Key_App_Name");
        down_url = intent.getStringExtra("Key_Down_Url");

        // create file,应该在这个地方加一个返回值的判断SD卡是否准备好，文件是否创建成功，等等！
        String path = android.os.Environment.getExternalStorageDirectory() + File.separator +
                      AppConst.CacheKeys.RootStorage + File.separator + app_name + ".apk";


        if (FileUtils.createFile(path)) {
            updateFile = new File(path);
        } else {
            return super.onStartCommand(intent, flags, startId);
        }

        createNotification();
        createThread();


        return super.onStartCommand(intent, flags, startId);
    }


    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_OK:
                    Uri uri = Uri.fromFile(updateFile);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri,"application/vnd.android.package-archive");
                    pendingIntent = PendingIntent.getActivity(VersionUpdateBLL.this, 0, intent, 0);

                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                    notification.setLatestEventInfo(VersionUpdateBLL.this,app_name, "down_sucess", pendingIntent);
                    notificationManager.notify(R.layout.widget_notification_item, notification);

                    //installApk();
                    stopSelf();
                    break;

                case DOWN_ERROR:
                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                    notification.setLatestEventInfo(VersionUpdateBLL.this,app_name, "down_fail", null);
                    stopSelf();
                    break;

                default:
                    stopSelf();
                    break;
            }
        }
    };

    private void installApk() {
        // TODO Auto-generated method stub
        Uri uri = Uri.fromFile(updateFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);

        /**********加这个属性是因为使用Context的startActivity方法的话，就需要开启一个新的task**********/
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.setDataAndType(uri,"application/vnd.android.package-archive");
        VersionUpdateBLL.this.startActivity(intent);
    }

    public void createThread() {
        new DownLoadThread().start();
    }


    private class DownLoadThread extends Thread{
        @Override
        public void run() {
            // TODO Auto-generated method stub
            Message message = new Message();
            try {
                long downloadSize = downloadUpdateFile(down_url, updateFile.toString());
                if (downloadSize > 0) {
                    // down success
                    message.what = DOWN_OK;
                    handler.sendMessage(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
                message.what = DOWN_ERROR;
                handler.sendMessage(message);
            }
        }
    }


    public void createNotification() {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        int color = VersionUpdateBLL.this.getResources().getColor(R.color.bella_ios_like_item_blue);

        notification = new Notification.Builder(VersionUpdateBLL.this)
            .setAutoCancel(true)
                .setContentTitle("EF Notification")
                .setSmallIcon(getNotificationIcon())
                .setLargeIcon(bm)
//                .setColor(Color.BLUE)
            .build();

        notification.flags = Notification.FLAG_ONGOING_EVENT;

        contentView = new RemoteViews(getPackageName(),R.layout.widget_notification_item);
        contentView.setTextViewText(R.id.notificationTitle, app_name + "is_downing");
        contentView.setTextViewText(R.id.notificationPercent, "0%");
        contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);
        notification.contentView = contentView;

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(R.layout.widget_notification_item, notification);
    }

    private int getNotificationIcon() {
        boolean whiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return whiteIcon ? R.drawable.test : R.drawable.ic_launcher;
    }

    public long downloadUpdateFile(String down_url, String file)throws Exception {

        int down_step = down_step_custom;// 提示step
        int totalSize;// 文件总大小
        int downloadCount = 0;
        int updateCount = 0;

        InputStream inputStream;
        OutputStream outputStream;

        URL url = new URL(down_url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setConnectTimeout(TIMEOUT);
        httpURLConnection.setReadTimeout(TIMEOUT);
        // 获取下载文件的size
        totalSize = httpURLConnection.getContentLength();

        if (httpURLConnection.getResponseCode() == 404) {
            throw new Exception("fail!");
        }

        inputStream = httpURLConnection.getInputStream();
        outputStream = new FileOutputStream(file, false);// 文件存在则覆盖掉

        byte buffer[] = new byte[1024 * 5];
        int readsize = 0;

        while ((readsize = inputStream.read(buffer)) != -1) {

            outputStream.write(buffer, 0, readsize);
            downloadCount += readsize;
            if (updateCount == 0 || ((downloadCount * 100) / totalSize - down_step) >= updateCount) {
                updateCount = downloadCount * 100 / totalSize;

                contentView.setTextViewText(R.id.notificationPercent,updateCount + "%");
                contentView.setProgressBar(R.id.notificationProgress, 100,updateCount, false);
                notification.contentView = contentView;
                notificationManager.notify(R.layout.widget_notification_item, notification);
            }
        }

        contentView.setTextViewText(R.id.notificationPercent,100 + "%");
        contentView.setProgressBar(R.id.notificationProgress, 100,100, false);
        notification.contentView = contentView;
        notificationManager.notify(R.layout.widget_notification_item, notification);


        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
        }
        inputStream.close();
        outputStream.close();

        return downloadCount;
    }

}