package test;

import org.json.JSONObject;

import cn.aurorax.dataunion.utils.HttpRequestUtil;

public class HttpRequestUtilTest {

	public static void main(String[] args) {
		String url="http://restapi.amap.com/v3/place/text";
		String data="key=7e798bda2744fa2e13465e2411cd5dbb&keywords=�˻�����&city=beijing&types=����סլ&citylimit=true";
		JSONObject json=HttpRequestUtil.httpRequest(url, "GET", data);
		System.out.println(json);
	}

}
