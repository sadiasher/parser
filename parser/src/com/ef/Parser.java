package com.ef;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.ef.pojo.RequestCount;
import com.ef.service.FileService;
import com.ef.service.RequestService;
import com.ef.service.impl.FileServiceImpl;
import com.ef.service.impl.RequestServiceImpl;

/**
 * Hello world!
 *
 */
public class Parser {

	//private final static String FILE_NAME = "access.log";

	static Logger logger = Logger.getLogger(Parser.class.getName());
	
	public static void main( String[] args ) {
		
		logger.log(Level.INFO, ">> In main method");
		
		FileService fileService = new FileServiceImpl();
		RequestService requestService = new RequestServiceImpl();

		if(ArrayUtils.isEmpty(args)) {
			logger.log(Level.SEVERE,"One or all parameters are missing.");
			return;
		}
		
		if(args.length < 4) {
			logger.log(Level.SEVERE,"One or all parameters are missing.");
			return;
		}
		
		//
		if(StringUtils.isEmpty(args[0]) || StringUtils.isEmpty(args[1]) || StringUtils.isEmpty(args[2]) || 
				StringUtils.isEmpty(args[3])) {
			logger.log(Level.SEVERE,"One or all parameters are missing.");
			return;
		}
		
		String fileNameMap =  args[0];
		String fileName = fileNameMap.split("=")[1];

		if(fileName.isEmpty()) {
			logger.log(Level.SEVERE,"File name paramter is missing");
			return;
		}

		try {

			if(fileService.isParsed(fileName)) {
				logger.log(Level.INFO, "File with name '"+fileName+"' was already parsed and stored in database");
				queryRequest(args, requestService);

			} else {
				logger.log(Level.INFO,"File with name '"+fileName+"' is going to be parsed");
				fileService.parseFile(fileName);
				
				queryRequest(args, requestService);

			}

		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
		}
	}

	private static void queryRequest(String[] args, RequestService requestService) throws Exception {
		
		logger.log(Level.INFO, " >> In queryRequest method ");
		
		String startDateMap = args[1];
		String durationMap = args[2];
		String thresholdMap = args[3];

		String startDate = startDateMap.split("=")[1];
		String duration = durationMap.split("=")[1];
		String threshold = thresholdMap.split("=")[1];

		logger.log(Level.INFO,"Start Date="+startDate+"  Duration="+duration+" Threshold="+threshold);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d.HH:mm:s");
		LocalDateTime startDateTime = LocalDateTime.parse(startDate, formatter);
		LocalDateTime endDateTime = null;

		if(duration.equals("hourly")) {
			endDateTime = startDateTime.plusHours(1);
		} else if(duration.equals("daily")) {

			int hh = startDateTime.getHour();
			int mm = startDateTime.getMinute();
			int ss = startDateTime.getSecond();

			startDateTime = startDateTime.minusHours(hh).minusMinutes(mm).minusSeconds(ss);
			endDateTime = startDateTime.plusHours(23l).plusMinutes(59).plusSeconds(59);

		}

		// Get requests details
		List<RequestCount> requestCounts = requestService.getRequestCount(startDateTime, endDateTime, Integer.parseInt(threshold));

		// Print requests details
		System.out.println("IP\t\tRequest Count");    		
		for (RequestCount requestCount : requestCounts) {
			System.out.println(requestCount.getIp()+"\t"+requestCount.getRequestCount());
		}

		// Save Request details
		String comments = "Request comes from infected IP.";
		requestService.save(requestCounts, comments, threshold, duration, startDateTime);
	}    

}
