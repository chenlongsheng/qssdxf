package cn.cdsoft.common.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cn.cdsoft.modules.homepage.service.DataViewService;

@Service
@Lazy(false)
public class DelHistotyDataTask {

	@Autowired
	DataViewService dataViewService;

	// 每天晚上1点执行
	@Scheduled(cron = "0 0 1 * * ?")
	private void process() {

		try {
			dataViewService.deleteHistoryData();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}