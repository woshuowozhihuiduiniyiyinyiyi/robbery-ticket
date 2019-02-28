package com.hj.tj.gohome.service.impl;

import com.hj.tj.gohome.service.StationService;
import com.hj.tj.gohome.vo.station.StationInfoReqObj;
import com.hj.tj.gohome.vo.station.StationInfoResObj;
import com.hj.tj.gohome.vo.station.StationTicketResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author tangj
 * @since 2019/2/28 11:07
 */
@Service
@Slf4j
public class StationServiceImpl implements StationService {

    private static final String URL_STR = "https://kyfw.12306.cn/otn/leftTicket/queryX?";
    private static final String TRAIN_DATE = "leftTicketDTO.train_date=";
    private static final String FROM_STATION = "leftTicketDTO.from_station=";
    private static final String TO_STATION = "leftTicketDTO.to_station=";
    private static final String PURPOSE_CODE = "purpose_codes=ADULT";
    private static final String COOKIE = "JSESSIONID=F4959E9490AEAA21A6E4407A90207300; route=c5c62a339e7744272a54643b3be5bf64; BIGipServerotn=1106248202.24610.0000; RAIL_EXPIRATION=1551570870843; RAIL_DEVICEID=TxpmWIrCfAI4FaVD9HF5shm1mTneQXWEu1OJ_p2Y7T2cRU6XRS8PaLcg1KLxR3cJhOHMwHWUOBzHJNSGE8tmA-MJoRHd8wAu2mMLc4kx0xS-OLmzc7-YSxZ8Yoovq3pCAdekhg5gkrPRJrdBL5vba6rv01l96PdB;";
    private static final Integer DEFAULT_TIME_OUT = 4000;

    @Override
    public List<StationInfoResObj> listStationInfo(StationInfoReqObj stationInfoReqObj) {
        StringBuilder urlStrBuffer = new StringBuilder(URL_STR);
        urlStrBuffer.append(TRAIN_DATE).append(stationInfoReqObj.getTrainDate())
                .append("&").append(FROM_STATION).append(stationInfoReqObj.getFromStation())
                .append("&").append(TO_STATION).append(stationInfoReqObj.getToStation())
                .append("&").append(PURPOSE_CODE);

        HttpsURLConnection connection = null;
        try {
            URL url = new URL(urlStrBuffer.toString());
            connection = (HttpsURLConnection) url.openConnection();
            connection.setConnectTimeout(DEFAULT_TIME_OUT);
            connection.setRequestProperty("Cookie", COOKIE);

            if (HttpStatus.OK.value() != connection.getResponseCode()) {
                log.error("connection 12306 error.status:{}", connection.getResponseCode());
                return new ArrayList<>();
            }

            List<String> strs = IOUtils.readLines(connection.getInputStream(), "UTF-8");
            StringBuilder stringBuilder = new StringBuilder();
            for (String str : strs) {
                stringBuilder.append(str);
            }

            System.out.println(stringBuilder.toString());
            JSONObject jsonObject = JSONObject.fromObject(stringBuilder.toString());
            StationTicketResponse stationTicketResponse = (StationTicketResponse) JSONObject.toBean(jsonObject, StationTicketResponse.class);

            List<StationInfoResObj> stationInfoResObjs = convertStationInfoResObjs(stationTicketResponse);

            return stationInfoResObjs;
        } catch (MalformedURLException e) {
            log.error("url is error.error stack:{}", e);
        } catch (IOException e) {
            log.error("open 12306 connection error.error stack:{}", e);
        } catch (JSONException e) {
            log.error("parse json error.e:{}", e);
        } finally {
            if (Objects.nonNull(connection)) {
                connection.disconnect();
            }
        }

        return new ArrayList<>();
    }

    private List<StationInfoResObj> convertStationInfoResObjs(StationTicketResponse stationTicketResponse) {
        List<StationInfoResObj> stationInfoResObjs = new ArrayList<>();

        List<String> responseList = stationTicketResponse.getData().getResult();
        for (String station : responseList) {
            StationInfoResObj stationInfoResObj = new StationInfoResObj();

            String[] splitResult = station.split("\\|");

            stationInfoResObj.setTrainNumber(splitResult[3]);
            stationInfoResObj.setBeginCity(splitResult[4]);
            stationInfoResObj.setEndCity(splitResult[5]);
            stationInfoResObj.setFromCity(splitResult[6]);
            stationInfoResObj.setToCity(splitResult[7]);
            stationInfoResObj.setFromTime(splitResult[8]);
            stationInfoResObj.setToTime(splitResult[9]);
            stationInfoResObj.setUsedTime(splitResult[10]);
            stationInfoResObj.setHasToday(splitResult[11]);

            List<String> ticketInfo = new ArrayList<>();
            // 高级软卧
            ticketInfo.add(splitResult[21]);
            // 软卧/一等卧
            ticketInfo.add(splitResult[23]);
            // 软座
            ticketInfo.add(splitResult[24]);
            // 无座
            ticketInfo.add(splitResult[26]);
            // 硬卧/二等卧
            ticketInfo.add(splitResult[28]);
            // 硬座
            ticketInfo.add(splitResult[29]);
            // 二等座
            ticketInfo.add(splitResult[30]);
            // 一等座
            ticketInfo.add(splitResult[31]);
            // 商务座/特等座
            ticketInfo.add(splitResult[32]);
            // 动卧
            ticketInfo.add(splitResult[33]);

            stationInfoResObj.setTicketInfo(ticketInfo);

            stationInfoResObjs.add(stationInfoResObj);
        }

        return stationInfoResObjs;
    }
}
