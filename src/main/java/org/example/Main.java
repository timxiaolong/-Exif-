package org.example;


import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class Main {
    public static void main(String[] args) {
        String path = "D:\\JAVA";		//要遍历的路径
        File file = new File(path);		//获取其file对象
        File[] fs = file.listFiles();	//遍历path下的文件和目录，放在File数组中
        for(File f:fs) {                    //遍历File[]数组
            //若非目录(即文件)，则操作
            if (!f.isDirectory()){
                changeExif(String.valueOf(f),"D:\\output");
            }
        }
    }
    //更改信息
    public static void changeExif(String inputPath, String outputPath){
        try {
            //读文件
            File file = new File(inputPath);
            //获取时间日期
            String dateTime = getTime(file);
            //获取ImageMetadata对象实例
            ImageMetadata metadata = Imaging.getMetadata(file);
            //强转为JpegImageMetadata
            JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
            //获取TiffImageMetadata
            TiffImageMetadata exif = jpegMetadata.getExif();
            //转换为流
            TiffOutputSet out = exif.getOutputSet();
            //获取TiffOutputDirectory
            TiffOutputDirectory exifDirectory = out.getOrCreateExifDirectory();
            //移除拍摄时间
            exifDirectory.removeField(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
            exifDirectory.removeField(ExifTagConstants.EXIF_TAG_DATE_TIME_DIGITIZED);
            //初始化时间
            //String date = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").format(new Date());
            //添加拍摄时间 格式为"yyyy:MM:dd HH:mm:ss"
            exifDirectory.add(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL, dateTime);
            exifDirectory.add(ExifTagConstants.EXIF_TAG_DATE_TIME_DIGITIZED, dateTime);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputPath));
            //写入新的图片
            new ExifRewriter().updateExifMetadataLossless(file, bos, out);
        } catch (Exception e) {
            //很多图片可能读取exif出现异常为正常现象 通常无需处理
            e.printStackTrace();
        }
    }
    //获取当前文件的名称并转换为时间字符串
    public static String getTime(File file){
        String name = file.getName();
        //IMG_20191116_175806.jpg
        String[] Times = name.split("_");

        return "success";
    }
}