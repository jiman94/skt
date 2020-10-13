package com.pilot.image.resize.imgscalr;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

public class ImgscalrExample {
	

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
		
	}
	
	
	


	public static BufferedImage simpleResizeImage(BufferedImage originalImage, int targetWidth) {
		return Scalr.resize(originalImage, targetWidth);
	}

	public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
		return Scalr.resize(originalImage, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, targetWidth, targetHeight,
				Scalr.OP_ANTIALIAS);
	}

	public static BufferedImage resizeImage2(BufferedImage originalImage, int targetWidth, int targetHeight)
			throws IOException {
		
		BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
		
		
		Graphics2D graphics2D = resizedImage.createGraphics();
		graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
		graphics2D.dispose();
		return resizedImage;
	}

	public static BufferedImage resizeImage3(BufferedImage originalImage, int targetWidth, int targetHeight)
			throws IOException {
		Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
		BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
		outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
		return outputImage;
	}

	

	

	public static void main1(String[] args) throws Exception {
		BufferedImage originalImage = ImageIO.read(new File("src/main/resources/images/9m.jpg"));
		BufferedImage outputImage = resizeImage(originalImage, 200, 200);
		ImageIO.write(outputImage, "jpg", new File("src/main/resources/resize/resized-imgscalr_200.jpg"));

		//
		originalImage = ImageIO.read(new File("src/main/resources/images/32tiff.tiff"));
		outputImage = resizeImage(originalImage, 200, 200);
		ImageIO.write(outputImage, "tiff", new File("src/main/resources/resize/resized-imgscalr_32tiff_200.tiff"));

		//
		originalImage = ImageIO.read(new File("src/main/resources/images/14png.png"));
		outputImage = resizeImage(originalImage, 200, 200);
		ImageIO.write(outputImage, "png", new File("src/main/resources/resize/resized-imgscalr_14png_200.png"));

		//
		originalImage = ImageIO.read(new File("src/main/resources/images/6m.jpg"));
		outputImage = resizeImage(originalImage, 200, 200);
		ImageIO.write(outputImage, "jpg", new File("src/main/resources/resize/resized-imgscalr_6m_200.jpg"));

		//
		originalImage = ImageIO.read(new File("src/main/resources/images/9m.jpg"));
		outputImage = resizeImage(originalImage, 200, 200);
		ImageIO.write(outputImage, "jpg", new File("src/main/resources/resize/resized-imgscalr_9m_200.jpg"));

	}
	
	

}
