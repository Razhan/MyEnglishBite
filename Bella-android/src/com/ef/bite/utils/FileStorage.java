package com.ef.bite.utils;

import java.io.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Log;

import com.ef.bite.AppConst;

public class FileStorage implements IFileStorage {

	private static final String TAG = "FileStorage";
	private File cacheDir; // the directory to save file

	public FileStorage(Context context, String uniqueName) {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory(),
					AppConst.CacheKeys.RootStorage + File.separator
							+ uniqueName);
		else
			cacheDir = context.getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();
	}

	/**
	 * 获得存储的跟文件夹
	 * 
	 * @return
	 */
	public String getStorageFolder() {
		return cacheDir.getAbsolutePath();
	}

    public File getStorageDir(){
        return cacheDir;
    }

	public Uri findFileUri(String key) {
		if (key.isEmpty())
			return null;
		File f = new File(cacheDir, key);
		if (!f.exists())
			return null;
		return Uri.fromFile(f);
	}

	@Override
	public String findFilePath(String key) {
		if (key.isEmpty())
			return null;
		File f = new File(cacheDir, key);
		if (!f.exists())
			return null;
		return f.getAbsolutePath();
	}

	@Override
	public File get(String key) {
		if (key.isEmpty())
			return null;
		File f = new File(cacheDir, key);
		if (f.exists()) {
			Log.i(TAG, "the file you wanted exists " + f.getAbsolutePath());
			return f;
		} else {
			Log.w(TAG,
					"the file you wanted does not exists: "
							+ f.getAbsolutePath());
			return null;
		}
	}

	/*
	 * 转换成Bitmap
	 */
	public Bitmap getBitmap(String key) {
		try {
			File f = get(key);
			if (f != null && f.exists())
				return BitmapFactory.decodeFile(f.getAbsolutePath());
			else
				return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/*
	 * 获得指定大小的图片缩略 thumbnail_size:缩小的倍数
	 */
	public Bitmap getScaledBitmap(String key, int newWidth, int newHeight) {
		Bitmap bitmap = null;
		try {
			bitmap = getBitmap(key);
			if (bitmap == null)
				return null;
			int w = bitmap.getWidth();
			int h = bitmap.getHeight();
			float scaleX = newWidth / (float) bitmap.getWidth();
			float scaleY = newHeight / (float) bitmap.getHeight();
			float pivotX = 0;
			float pivotY = 0;
			Matrix mtx = new Matrix();
			mtx.setScale(scaleX, scaleY, pivotX, pivotY);
			return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
		} catch (Exception ex) {
			Log.e(TAG, "Fail to get Bitmap thumbnail for localpath: " + key);
			Log.e(TAG, ex.getMessage());
			return null;
		} finally {
			if (bitmap != null) {
				bitmap.recycle();
				bitmap = null;
			}
		}
	}

	/*
	 * 缩小图片大小倍数
	 */
	public Bitmap getScaledBitmap(String key, int inSampleSize) {
		File f = get(key);
		if (f == null)
			return null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(f.getAbsolutePath(), options);
			options.inJustDecodeBounds = false;

			options.outWidth = options.outWidth / inSampleSize;
			options.outHeight = options.outHeight / inSampleSize;
			if (options.inSampleSize < 1)
				options.inSampleSize = 1;
			else
				options.inSampleSize = inSampleSize;
			return BitmapFactory.decodeFile(f.getAbsolutePath(), options);
		} catch (Exception ex) {
			Log.e(TAG, "Fail to get Bitmap thumbnail for localpath: " + key);
			Log.e(TAG, ex.getMessage());
			return null;
		}
	}

	@Override
	public void put(String key, File value) {
		if (key != null && !"".equals(key) && value != null) {
			File f = new File(cacheDir, key);
			if (f.exists())
				f.delete();
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// Use the util's function to save the bitmap.
			if (copy(value, f))
				Log.d(TAG, "Save file to sdcard successfully!");
			else
				Log.w(TAG, "Save file to sdcard failed!");
		}
	}

	/*
	 * 保存文件流
	 */
	public void put(String key, InputStream in) {
		if (in == null)
			return;
		try {
			File f = new File(cacheDir, key);
			if (f.exists())
				f.delete();
			f.createNewFile();
			OutputStream out = new FileOutputStream(f);
			// Transfer bytes from in to out
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		} catch (Exception ex) {
			Log.e(TAG, "Fail to put the file stream as key: " + key);
			ex.printStackTrace();
		}
	}

	@Override
	public void delete(String key) {
		File file = get(key);
		if (file != null)
			file.delete();
	}

	@Override
	public void clear() {
		File[] files = cacheDir.listFiles();
		for (File f : files)
			f.delete();
	}

	/**
	 * 清空过期的文件
	 * 
	 * @param expiredTime
	 *            过期时间
	 */
	public void clearExpiredFiles(long expiredTime) {
		File[] files = cacheDir.listFiles();
		if (files != null && files.length > 0) {
			long currentTime = System.currentTimeMillis();
			for (File f : files) {
				if ((currentTime - f.lastModified()) >= expiredTime) { // 过期了
					f.delete();
				}
			}
		}
	}

	private boolean copy(File src, File dst) {
		try {
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dst);

			// Transfer bytes from in to out
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 清空目录下所有文件
	 */
	public void clearAll() {
		File[] files = cacheDir.listFiles();
		for (File f : files)
			deleteDir(f);
	}

	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 * 
	 * @param dir
	 *            将要删除的文件目录
	 * @return boolean Returns "true" if all deletions were successful. If a
	 *         deletion fails, the method stops attempting to delete and returns
	 *         "false".
	 */
	private static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			// 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}

    /**
     * 创建.nomedia来排除媒体库扫描
     * @return
     */
    public boolean createNomediaFile(){
        try {
            return new File(cacheDir, ".nomedia").createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void CopyAssets(String assetDir,String dir, Context mcontext) {
        String[] files;
        try {
            files = mcontext.getResources().getAssets().list(assetDir);
        } catch (IOException e1) {
            return;
        }
        File mWorkingPath = new File(dir);
        //if this directory does not exists, make one.
        if (!mWorkingPath.exists()) {
            if (!mWorkingPath.mkdirs()) {

            }
        }

        for (int i = 0; i < files.length; i++) {
            try {
                String fileName = files[i];
                //we make sure file name not contains '.' to be a folder.
                if (!fileName.contains(".")) {
                    if (0 == assetDir.length()) {
                        CopyAssets(fileName, dir + fileName + "/", mcontext);
                    } else {
                        CopyAssets(assetDir + "/" + fileName, dir + fileName + "/", mcontext);
                    }
                    continue;
                }
                File outFile = new File(mWorkingPath, fileName);
                if (outFile.exists())
                    outFile.delete();
                InputStream in = null;
                if (0 != assetDir.length())
                    in = mcontext.getAssets().open(assetDir + "/" + fileName);
                else
                    in = mcontext.getAssets().open(fileName);
                OutputStream out = new FileOutputStream(outFile);

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                in.close();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
