package cn.cdsoft.common.filter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 解决中文乱码的过滤器
 */
public class EncodingConvertFilter extends OncePerRequestFilter {
	
	private String fromEncoding = "ISO-8859-1";
	private String toEncoding = "UTF-8";

	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain) {
		try {
			request.setCharacterEncoding(toEncoding);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		if (request.getMethod().equalsIgnoreCase("GET")) {
			Iterator<String[]> localIterator = request.getParameterMap().values().iterator();
			while (localIterator.hasNext()) {
				String[] arrayOfString = (String[]) localIterator.next();
				for (int i = 0; i < arrayOfString.length; i++){
					try {
						arrayOfString[i] = URLDecoder.decode(arrayOfString[i],"UTF-8");
					} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
						localUnsupportedEncodingException.printStackTrace();
					}
				}
			}
		}
		
		try {
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getFromEncoding() {
		return this.fromEncoding;
	}

	public void setFromEncoding(String fromEncoding) {
		this.fromEncoding = fromEncoding;
	}

	public String getToEncoding() {
		return this.toEncoding;
	}

	public void setToEncoding(String toEncoding) {
		this.toEncoding = toEncoding;
	}
}
