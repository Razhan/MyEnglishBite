package com.ef.bite.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

	public static Map<String, String> getContentMapByZip(String zip_file_name,
			String encoding) {
		Map<String, String> file_content_map = new HashMap<String, String>();
		File file = new File(zip_file_name);
		try {
			ZipFile zip = new ZipFile(file);
			Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip
					.entries();

			while (entries.hasMoreElements()) {
				ZipEntry ze = (ZipEntry) entries.nextElement();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						zip.getInputStream(ze), encoding));

				String content = "";
				String line = "";
				while ((line = br.readLine()) != null) {
					content = content + line;
				}
				file_content_map.put(ze.getName(), content);

				br.close();
			}

		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return file_content_map;
	}

	// 解压文件
	@SuppressWarnings({ "resource", "unchecked" })
	public static boolean decompress(String zipPath, String targetPath,
			String encoding) {
		// 迭代中的每一个条目
		ZipEntry entry = null;
		// 解压后的文件
		File outFile = null;
		// 读取压缩文件的输入流
		BufferedInputStream bin = null;
		// 写入解压后文件的输出流
		BufferedOutputStream bout = null;
		try {
			// 获取解缩文件
			File file = new File(zipPath);
			if (!file.isFile()) {
				throw new FileNotFoundException("要解压的文件不存在");
			}
			// 设置解压路径
			if (targetPath == null || "".equals(targetPath)) {
				targetPath = file.getParent();
			}
			// 设置解压编码
			if (encoding == null || "".equals(encoding)) {
				encoding = "GBK";
			}
			// 实例化ZipFile对象
			ZipFile zipFile = new ZipFile(file);
			// 获取ZipFile中的条目
			Enumeration<ZipEntry> files = (Enumeration<ZipEntry>) zipFile
					.entries();
			while (files.hasMoreElements()) {
				// 获取解压条目
				entry = files.nextElement();
				// 实例化解压后文件对象
				outFile = new File(targetPath + File.separator
						+ entry.getName());
				// 如果条目为目录，则跳向下一个
				if (entry.getName().endsWith(File.separator)) {
					if (!outFile.exists())
						outFile.mkdirs();
					continue;
				}
				// 创建目录
				if (!outFile.getParentFile().exists()) {
					outFile.getParentFile().mkdirs();
				}
				if (outFile.exists())
					outFile.delete();
				// 创建新文件
				outFile.createNewFile();
				// 如果不可写，则跳向下一个条目
				if (!outFile.canWrite()) {
					continue;
				}
				// 获取读取条目的输入流
				bin = new BufferedInputStream(zipFile.getInputStream(entry));
				// 获取解压后文件的输出流
				bout = new BufferedOutputStream(new FileOutputStream(outFile));
				// 读取条目，并写入解压后文件
				byte[] buffer = new byte[1024];
				int readCount = -1;
				while ((readCount = bin.read(buffer)) != -1) {
					bout.write(buffer, 0, readCount);
				}
				bout.flush();
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			try {
				if (bin != null)
					bin.close();
				if (bout != null)
					bout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 压缩文件
	public static void compress(String sourcePath, String zipPath,
			String encoding, String comment) throws FileNotFoundException,
			IOException {
		// 判断要压缩的文件是否存在
		File sourceFile = new File(sourcePath);
		if (!sourceFile.exists()
				|| (sourceFile.isDirectory() && sourceFile.list().length == 0)) {
			throw new FileNotFoundException("要压缩的文件或目录不存在，或者要压缩的目录为空");
		}
		// 设置压缩文件路径，默认为将要压缩的路径的父目录为压缩文件的父目录
		if (zipPath == null || "".equals(zipPath)) {
			String sourcePathName = sourceFile.getAbsolutePath();
			int index = sourcePathName.lastIndexOf(".");
			zipPath = (index > -1 ? sourcePathName.substring(0, index)
					: sourcePathName) + ".zip";
		} else {
			// 如果压缩路径为目录，则将要压缩的文件或目录名做为压缩文件的名字，这里压缩路径不以“.zip”为结尾则认为压缩路径为目录
			if (!zipPath.endsWith(".zip")) {
				// 如果将要压缩的路径为目录，则以此目录名为压缩文件名；如果将要压缩的路径为文件，则以此文件名（去除扩展名）为压缩文件名
				String fileName = sourceFile.getName();
				int index = fileName.lastIndexOf(".");
				zipPath = zipPath
						+ File.separator
						+ (index > -1 ? fileName.substring(0, index) : fileName)
						+ ".zip";
			}
		}
		// 设置解压编码
		if (encoding == null || "".equals(encoding)) {
			encoding = "GBK";
		}
		// 要创建的压缩文件的父目录不存在，则创建
		File zipFile = new File(zipPath);
		if (!zipFile.getParentFile().exists()) {
			zipFile.getParentFile().mkdirs();
		}
		// 创建压缩文件输出流
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(zipPath);
		} catch (FileNotFoundException e) {

		} finally {
			if (fos != null)
				fos.close();
		}
		// 使用指定校验和创建输出流
		CheckedOutputStream csum = new CheckedOutputStream(fos, new CRC32());
		// 创建压缩流
		ZipOutputStream zos = new ZipOutputStream(csum);
		// 设置编码，支持中文
		// zos.setEncoding(encoding);
		// 设置压缩包注释
		zos.setComment(comment);
		// 启用压缩
		zos.setMethod(ZipOutputStream.DEFLATED);
		// 设置压缩级别为最强压缩
		zos.setLevel(Deflater.BEST_COMPRESSION);
		// 压缩文件缓冲流
		BufferedOutputStream bout = null;
		try {
			// 封装压缩流为缓冲流
			bout = new BufferedOutputStream(zos);
			// 对数据源进行压缩
			compressRecursive(zos, bout, sourceFile, sourceFile.getParent());
		} finally {
			if (bout != null) {
				try {
					bout.close();
				} catch (Exception e) {
				}
			}
		}
	}

	// 压缩文件时，所使用的迭代方法
	private static void compressRecursive(ZipOutputStream zos,
			BufferedOutputStream bout, File sourceFile, String prefixDir)
			throws IOException, FileNotFoundException {
		// 获取压缩条目名，初始时将要压缩的文件或目录的相对路径
		String entryName = sourceFile.getAbsolutePath().substring(
				prefixDir.length() + File.separator.length());
		// 判断是文件还是目录，如果是目录，则继续迭代压缩
		if (sourceFile.isDirectory()) {
			// 如果是目录，则需要在目录后面加上分隔符('/')
			// ZipEntry zipEntry = new ZipEntry(entryName + File.separator);
			// zos.putNextEntry(zipEntry);
			// 获取目录中的文件，然后迭代压缩
			File[] srcFiles = sourceFile.listFiles();
			for (int i = 0; i < srcFiles.length; i++) {
				// 压缩
				compressRecursive(zos, bout, srcFiles[i], prefixDir);
			}
		} else {
			// 开始写入新的ZIP文件条目并将流定位到条目数据的开始处
			ZipEntry zipEntry = new ZipEntry(entryName);
			// 向压缩流中写入一个新的条目
			zos.putNextEntry(zipEntry);
			// 读取将要压缩的文件的输入流
			BufferedInputStream bin = null;
			try {
				// 获取输入流读取文件
				bin = new BufferedInputStream(new FileInputStream(sourceFile));
				// 读取文件，并写入压缩流
				byte[] buffer = new byte[1024];
				int readCount = -1;
				while ((readCount = bin.read(buffer)) != -1) {
					bout.write(buffer, 0, readCount);
				}
				// 注，在使用缓冲流写压缩文件时，一个条件完后一定要刷新，不然可能有的内容就会存入到后面条目中去了
				bout.flush();
				// 关闭当前ZIP条目并定位流以写入下一个条目
				zos.closeEntry();
			} finally {
				if (bin != null) {
					try {
						bin.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}

	// 压缩多个文件
	public static void compress(List<String> sourcePaths, String zipPath,
			String encoding, String comment) throws FileNotFoundException,
			IOException {
		// 设置压缩文件路径，默认为将要压缩的路径的父目录为压缩文件的父目录
		if (zipPath == null || "".equals(zipPath) || !zipPath.endsWith(".zip")) {
			throw new FileNotFoundException("必须指定一个压缩路径，而且该路径必须以'.zip'为结尾");
		}
		// 设置解压编码
		if (encoding == null || "".equals(encoding)) {
			encoding = "GBK";
		}
		// 要创建的压缩文件的父目录不存在，则创建
		File zipFile = new File(zipPath);
		if (!zipFile.getParentFile().exists()) {
			zipFile.getParentFile().mkdirs();
		}
		// 创建压缩文件输出流
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(zipPath);
		} catch (FileNotFoundException e) {

		} finally {
			if (fos != null)
				fos.close();
		}
		// 使用指定校验和创建输出流
		CheckedOutputStream csum = new CheckedOutputStream(fos, new CRC32());
		// 创建压缩流
		ZipOutputStream zos = new ZipOutputStream(csum);
		// 设置编码，支持中文
		// zos.setEncoding(encoding);
		// 设置压缩包注释
		zos.setComment(comment);
		// 启用压缩
		zos.setMethod(ZipOutputStream.DEFLATED);
		// 设置压缩级别为最强压缩
		zos.setLevel(Deflater.BEST_COMPRESSION);
		// 压缩文件缓冲流
		BufferedOutputStream bout = null;
		try {
			// 封装压缩流为缓冲流
			bout = new BufferedOutputStream(zos);
			// 迭代压缩每一个路径
			for (int i = 0, len = sourcePaths.size(); i < len; i++) {
				// 获取每一个压缩路径
				File sourceFile = new File(sourcePaths.get(i));
				// 对数据源进行压缩
				compressRecursive(zos, bout, sourceFile, sourceFile.getParent());
			}
		} finally {
			if (bout != null) {
				try {
					bout.close();
				} catch (Exception e) {
				}
			}
		}
	}

}
