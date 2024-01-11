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
        try {
            //读文件
            File file = new File("C:\\Users\\53021\\Desktop\\IMG_20191116_175806.jpg");
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
            exifDirectory.add(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL, "2024:01:01 12:01:01");
            exifDirectory.add(ExifTagConstants.EXIF_TAG_DATE_TIME_DIGITIZED, "2024:01:01 12:01:01");
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("D:\\IMG_20240101_120101.jpg"));
            //写入新的图片
            new ExifRewriter().updateExifMetadataLossless(file, bos, out);
        } catch (Exception e) {
            //很多图片可能读取exif出现异常为正常现象 通常无需处理
            e.printStackTrace();
        }
    }
}