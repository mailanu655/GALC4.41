package com.honda.mfg.stamp.storage.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.honda.mfg.stamp.conveyor.domain.StampingImage;


@Controller
public class ImageController {

	private static final Logger LOG = LoggerFactory.getLogger(ImageController.class);

	@RequestMapping(value = "/resources/images/{imageName}.{ext}")
	public void getImage(@PathVariable String imageName, @PathVariable String ext,HttpServletRequest request, HttpServletResponse response)  {
		StampingImage thisImage = StampingImage.findStampingImage(imageName+"."+ext);
		byte[] bytes = null;
		if(thisImage==null||thisImage.getImageBytes()==null || thisImage.getImageBytes().length<1){
			try {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			bytes = thisImage.getImageBytes();
			response.setContentType("image/"+((ext.equalsIgnoreCase("jpg"))?"jpeg":ext.toLowerCase()));
			try{
				response.getOutputStream().write(bytes);
				response.flushBuffer();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	@RequestMapping(value = "/resources/images/putTestImage")
	@ResponseBody
	public String putTestImage(HttpServletRequest request){
		LOG.info("TEST IMAGE INSERTED BY: " + request.getUserPrincipal().getName()+", IMAGE NAME: EMPTY.png");

		byte b[] = getFileBytes("../../images/EMPTY.png");
	
		StampingImage image = new StampingImage();
		image.setImageBytes( b);
		image.setImageName("EMPTY.png");
		try{
			image.merge();
		}catch(Exception e){
			e.printStackTrace();
		}
		return "Done";
	}
	private byte[] getFileBytes(String filename){
		byte b[] = new byte[4096];  
		ByteArrayOutputStream ous = null;InputStream in = null;
		int readBytes = 0;

		try {
			ous = new ByteArrayOutputStream();
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
			while ((readBytes = in.read(b)) != -1) {
				ous.write(b, 0, readBytes);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
			
		}finally {
			try {
				if (ous != null)
					ous.close();
			} catch (IOException e) {
			}

			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
			}
		}
		return ous.toByteArray();
	}
	@RequestMapping(value="/uploadImage", method=RequestMethod.POST)
	public String handleFileUpload(Model uiModel,
			@RequestParam("file") MultipartFile file, HttpServletRequest request){
		String result = "";
		if (file!=null && !file.isEmpty()&&file.getOriginalFilename()!=null && !file.getOriginalFilename().isEmpty()) {
			String name = file.getOriginalFilename();
			String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			if(fileExt!=null && !fileExt.isEmpty() && (fileExt.equalsIgnoreCase(".bmp")|| fileExt.equalsIgnoreCase(".png")|| fileExt.equalsIgnoreCase(".gif")||fileExt.equalsIgnoreCase(".jpg")||fileExt.equalsIgnoreCase(".tiff"))){
				try {
					byte[] bytes = file.getBytes();
					if(bytes!=null && bytes.length>0){
						StampingImage image = new StampingImage();
						image.setImageBytes( bytes);
						image.setImageName(file.getOriginalFilename());
						image.merge();
						LOG.info("NEW IMAGE INSERTED BY: " + request.getUserPrincipal().getName()+", IMAGE NAME: " + name);
						result = "Upload of " + name + " successful.";
					}else{
						LOG.error("0 length image upload attempt by: " + request.getUserPrincipal().getName()+", IMAGE NAME: " + name);
						result = "Zero File Length Not Allowed.";
					}
				} catch (Exception e) {
					LOG.error("Error Uploading Image " + e.getMessage() + " by: " + request.getUserPrincipal().getName()+", IMAGE NAME: " + name, e);
					result ="You failed to upload " + name + " => " + e.getMessage();
				}
			}else{
				result = "Only image files allowed";
				LOG.error("Wrong Image file Type by: " + request.getUserPrincipal().getName()+", IMAGE NAME: " + name);

			}
		} else {
			LOG.error("Empty File image upload by: " + request.getUserPrincipal().getName());
			result= "You failed to upload because the file was empty.";
		}
		List<String> lst = StampingImage.findImageNames();
		Collections.sort(lst, new Comparator<String>(){

			@Override
			public int compare(String o1, String o2) {
				if(o1==null){
					if(o2==null)return 0;
					else return -1;
				}
				return o1.toLowerCase().compareTo(o2.toLowerCase());
			}
			
		});
		uiModel.addAttribute("currentImages",lst);
		uiModel.addAttribute("uploadResult",result);
		return "uploadImage";
	}
	@RequestMapping(value="/uploadImage", method = RequestMethod.GET)
	public String openUploadPage(Model uiModel){
		List<String> lst = StampingImage.findImageNames();
		Collections.sort(lst, new Comparator<String>(){

			@Override
			public int compare(String o1, String o2) {
				if(o1==null){
					if(o2==null)return 0;
					else return -1;
				}
				return o1.toLowerCase().compareTo(o2.toLowerCase());
			}
			
		});
		//	System.out.println("Loading Upload Page");
		uiModel.addAttribute("uploadResult","Select a file to upload.");
		uiModel.addAttribute("currentImages",lst);
		return "uploadImage";
	}
}
