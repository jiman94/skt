package com.pilot.image.resize.imgscalr;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ScalrImg {
	

	public static void imgResize(String imgSourcePath, String imgTargetPath, String imgFormat, int newWidth, int newHeight) {
	    try {
	        // 원본 이미지 가져오기
	        Image imgSrc = ImageIO.read(new File(imgSourcePath));
	        // 이미지 리사이즈
	        // Image.SCALE_DEFAULT : 기본 이미지 스케일링 알고리즘 사용
	        // Image.SCALE_FAST    : 이미지 부드러움보다 속도 우선
	        // Image.SCALE_SMOOTH  : 속도보다 이미지 부드러움을 우선
	        // Image.SCALE_AREA_AVERAGING  : 평균 알고리즘 사용
	        Image resizeImage = imgSrc.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
	        // 새 이미지  저장하기
	        BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
	        Graphics g = newImage.getGraphics();
	        g.drawImage(resizeImage, 0, 0, null);
	        g.dispose();	        
	        ImageIO.write(newImage, imgFormat, new File(imgTargetPath));
	    }
	    catch (Exception e)
	    {
	    }
	}


	public static void main(String[] args) throws Exception {
		
		String imgSourcePath= "src/main/resources/images/";
		String imgTargetPath= "src/main/resources/resize/"; 
		String imgFormat = "jpg";        // 이미지 포맷. JPG, BMP, JPEG, WBMP, PNG, GIF

		imgResize(imgSourcePath+"9m.jpg", imgTargetPath+"resized-imgscalr_200.jpg", "jpg", 200, 200);
		imgResize(imgSourcePath+"32tiff.tiff", imgTargetPath+"resized-imgscalr_200_32tiff_200.tiff", "tiff", 200, 200);
		imgResize(imgSourcePath+"14png.png", imgTargetPath+"resized-imgscalr_200_14png.png", "png", 200, 200);
		imgResize(imgSourcePath+"9m.jpg", imgTargetPath+"resized-imgscalr_200_9m.jpg", "jpg", 200, 200);
		
		imgResize(imgSourcePath+"7mwbmp.webp", imgTargetPath+"resized-imgscalr_200_9m.webp", "WBMP", 200, 200);
		imgResize(imgSourcePath+"36mbmp.bmp", imgTargetPath+"resized-imgscalr_200_36mbmp.bmp", "bmp", 200, 200);
		imgResize(imgSourcePath+"2mgif.gif", imgTargetPath+"resized-imgscalr_200_2mgif.gif", "gif", 200, 200);
		
	}
		

}
