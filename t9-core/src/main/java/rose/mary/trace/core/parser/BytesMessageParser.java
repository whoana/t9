/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.parser;

import java.io.ByteArrayInputStream;
import java.util.Map;

import javax.jms.BytesMessage;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
 

import pep.per.mint.common.util.Util;
import rose.mary.trace.core.data.common.Trace;
import rose.mary.trace.core.helper.module.mte.MTEStruct;

/**
 * <pre>
 * rose.mary.trace.parser
 * ByteMessageParser.java
 * </pre>
 * @author whoana
 * @date Aug 16, 2019
 */
public class BytesMessageParser extends Parser {

	Map<String, Integer> nodeMap;
	
	public BytesMessageParser() {}
	
	public BytesMessageParser(Map<String, Integer> nodeMap) {
		
		this.nodeMap = nodeMap;
	}
	@Override
	public Trace parse(Object traceObject) throws Exception {
		
		BytesMessage msg = (BytesMessage) traceObject;
		
		byte[] strucId = new byte[4];
		msg.readBytes(strucId);
		int version = msg.readInt();
		int strucLength = msg.readInt();
		int encoding = msg.readInt();
		int codedCharSetId = msg.readInt();
		byte[] format = new byte[8];
		msg.readBytes(format);
		int flags = msg.readInt();
		int nameValueCCSID = msg.readInt();
		int mcdLength = msg.readInt();
		byte[] mcd = new byte[mcdLength];
		msg.readBytes(mcd);
		int dataLength = msg.readInt();
		byte[] usrData = new byte[dataLength];
		msg.readBytes(usrData);
		
		Trace trace = parseUsrData(usrData);
 
		trace.setId(Util.join(trace.getIntegrationId(),idSeperator, trace.getDate() ,idSeperator, trace.getHostId(), idSeperator, trace.getProcessId()));	
		
		if(nodeMap != null) {
			 trace.setSeq(nodeMap.getOrDefault(trace.getType(), 0));
		}
		
		trace.setProcessEndDate(trace.getProcessDate());
		
		
		return trace;
	}

	/**
	 * @param usrData
	 * @return
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	private Trace parseUsrData(byte[] usrData) throws Exception {
		
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		
		ByteArrayInputStream is = new  ByteArrayInputStream(usrData);
		
		StringBuffer buffer = new StringBuffer();
		 
		Trace trace = new Trace();
		
		DefaultHandler dh = new DefaultHandler() {
			
			String category = null;
			String field = null; 
			public void startDocument() throws SAXException {
				
			}
			
			public void endDocument() throws SAXException {
				
			}
			
			public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
				//reset field
				field = null;
				
				if(qName.equalsIgnoreCase(MTEStruct.a)) {
					category = MTEStruct.a;
				}else if(qName.equalsIgnoreCase(MTEStruct.b)) { 
					category = MTEStruct.b;
				}else if(qName.equalsIgnoreCase(MTEStruct.c)) { 
					category = MTEStruct.c;
				}else if(qName.equalsIgnoreCase(MTEStruct.d)) {    
					category = MTEStruct.d;
//				}else if(qName.equalsIgnoreCase(MTEStruct.e)) { 				   
//					category = MTEStruct.e;
//				}else if(qName.equalsIgnoreCase(MTEStruct.f)) { 				   
//					category = MTEStruct.f;
				}else if(qName.equalsIgnoreCase(MTEStruct.g)) { 				   
					category = MTEStruct.g;
//				}else if(qName.equalsIgnoreCase(MTEStruct.h)) { 				   
//					category = MTEStruct.h;
//				}else if(qName.equalsIgnoreCase(MTEStruct.i)) { 				   
//					category = MTEStruct.i;
				} else {
					if(MTEStruct.a.equals(category)) { 
						if(qName.equalsIgnoreCase(MTEStruct.a_host_id))             field = MTEStruct.a_host_id;
						else if(qName.equalsIgnoreCase(MTEStruct.a_group_id))       field = MTEStruct.a_group_id;
						else if(qName.equalsIgnoreCase(MTEStruct.a_intf_id))        field = MTEStruct.a_intf_id;
						else if(qName.equalsIgnoreCase(MTEStruct.a_date)) 	        field = MTEStruct.a_date;
						else if(qName.equalsIgnoreCase(MTEStruct.a_time)) 	        field = MTEStruct.a_time;
						else if(qName.equalsIgnoreCase(MTEStruct.a_global_id))      field = MTEStruct.a_global_id;
					}else if(MTEStruct.j.equals(category)) { 
						if(qName.equalsIgnoreCase(MTEStruct.j_host_id))             field = MTEStruct.j_host_id;
						else if(qName.equalsIgnoreCase(MTEStruct.j_process_id))     field = MTEStruct.j_process_id;
					}else if(MTEStruct.b.equals(category)) { 
						if(qName.equalsIgnoreCase(MTEStruct.b_host_id))             field = MTEStruct.b_host_id;
						else if(qName.equalsIgnoreCase(MTEStruct.b_os_type))        field = MTEStruct.b_os_type;
						else if(qName.equalsIgnoreCase(MTEStruct.b_os_version))     field = MTEStruct.b_os_version;
					}else if(MTEStruct.c.equals(category)) {
						if(qName.equalsIgnoreCase(MTEStruct.c_date))        		field = MTEStruct.c_date;
						else if(qName.equalsIgnoreCase(MTEStruct.c_time))   		field = MTEStruct.c_time;
						else if(qName.equalsIgnoreCase(MTEStruct.c_process_mode))	field = MTEStruct.c_process_mode;
						else if(qName.equalsIgnoreCase(MTEStruct.c_process_type))   field = MTEStruct.c_process_type;
						else if(qName.equalsIgnoreCase(MTEStruct.c_process_id))		field = MTEStruct.c_process_id;
						else if(qName.equalsIgnoreCase(MTEStruct.c_hub_cnt))   		field = MTEStruct.c_hub_cnt;
						else if(qName.equalsIgnoreCase(MTEStruct.c_spoke_cnt))		field = MTEStruct.c_spoke_cnt;
						else if(qName.equalsIgnoreCase(MTEStruct.c_recv_spoke_cnt)) field = MTEStruct.c_recv_spoke_cnt;
						else if(qName.equalsIgnoreCase(MTEStruct.c_hop_cnt))		field = MTEStruct.c_hop_cnt;
						else if(qName.equalsIgnoreCase(MTEStruct.c_time))   		field = MTEStruct.c_time;
						else if(qName.equalsIgnoreCase(MTEStruct.c_appl_type))		field = MTEStruct.c_appl_type;
						else if(qName.equalsIgnoreCase(MTEStruct.c_timezone))		field = MTEStruct.c_timezone;
						else if(qName.equalsIgnoreCase(MTEStruct.c_elaspsed_time))	field = MTEStruct.c_elaspsed_time;
					}else if(MTEStruct.d.equals(category)) {
						if(qName.equalsIgnoreCase(MTEStruct.d_status))         	  	field = MTEStruct.d_status;
						else if(qName.equalsIgnoreCase(MTEStruct.d_error_type))     field = MTEStruct.d_error_type;
						else if(qName.equalsIgnoreCase(MTEStruct.d_error_code))     field = MTEStruct.d_error_code;
						else if(qName.equalsIgnoreCase(MTEStruct.d_error_message))  field = MTEStruct.d_error_message;
//					}else if(MTEStruct.e.equals(category)) {
//					}else if(MTEStruct.f.equals(category)) {
					}else if(MTEStruct.g.equals(category)) {
						if(qName.equals(MTEStruct.g_data_size))                     field = MTEStruct.g_data_size;
						else if(qName.equalsIgnoreCase(MTEStruct.g_record_cnt))     field = MTEStruct.g_record_cnt;
						else if(qName.equalsIgnoreCase(MTEStruct.g_data_compress))  field = MTEStruct.g_data_compress;
//					}else if(MTEStruct.h.equals(category)) {
//					}else if(MTEStruct.i.equals(category)) {
					}
				}
				 
				
				buffer.setLength(0); 
			}
			//todo : categroy field null check
			public void characters(char[] ch, int start, int length) throws SAXException {
				
				buffer.append(ch, start, length); 
				
//				String ca = category;
//				String fi = field;
//				String value = buffer.toString().trim();
//				
//				System.out.println("category:" + category);
//				System.out.println("field:" + field);
//				System.out.println("value:" + value);
				
				if(MTEStruct.a.equals(category) && field != null) {
					if(field.equals(MTEStruct.a_host_id))  		     trace.setOriginHostId(buffer.toString().trim());
					else if(field.equals(MTEStruct.a_intf_id))       trace.setIntegrationId(buffer.toString().trim());
					else if(field.equals(MTEStruct.a_date)) 	     trace.setDate(buffer.toString().trim());
					else if(field.equals(MTEStruct.a_time)) 	     trace.setDate(trace.getDate() + buffer.toString().trim());
				}else if(MTEStruct.j.equals(category) && field != null) { 
					if(field.equals(MTEStruct.j_host_id))   	     trace.setPreviousHostId(buffer.toString().trim());
					else if(field.equals(MTEStruct.j_process_id))    trace.setPreviousProcessId(buffer.toString().trim());					
				}else if(MTEStruct.b.equals(category) && field != null) { 
					if(field.equals(MTEStruct.b_os_type))   	     trace.setOs(buffer.toString().trim());
					else if(field.equals(MTEStruct.b_os_version))    trace.setOs(trace.getOs() + buffer.toString().trim());
					else if(field.equals(MTEStruct.b_host_id))		 trace.setHostId(buffer.toString().trim());
				}else if(MTEStruct.c.equals(category) && field != null) {
					if(field.equals(MTEStruct.c_date))        		 trace.setProcessDate(buffer.toString().trim());
					else if(field.equals(MTEStruct.c_time))   		 trace.setProcessDate(trace.getProcessDate() + buffer.toString().trim());
					else if(field.equals(MTEStruct.c_process_mode))  trace.setType(buffer.toString().trim());
					else if(field.equals(MTEStruct.c_process_id))	 trace.setProcessId(buffer.toString().trim());
					else if(field.equals(MTEStruct.c_timezone))	     trace.setTimezone(buffer.toString().trim());
					else if(field.equals(MTEStruct.c_elaspsed_time)) trace.setElapsedTime(buffer.toString().trim());
					else if(field.equals(MTEStruct.c_recv_spoke_cnt)) {
						try {
							trace.setTodoNodeCount(Integer.parseInt(buffer.toString().trim()));
						}catch(Exception e) {
							trace.setTodoNodeCount(1);
						}
					}
				}else if(MTEStruct.d.equals(category) && field != null) {
					if(field.equals(MTEStruct.d_status))         	 trace.setStatus(buffer.toString().trim());
					else if(field.equals(MTEStruct.d_error_code))    trace.setErrorCode(buffer.toString().trim());
					else if(field.equals(MTEStruct.d_error_message)) trace.setErrorMessage(buffer.toString().trim());
				}else if(MTEStruct.g.equals(category) && field != null) {
					if(field.equals(MTEStruct.g_data_size))          trace.setDataSize(Integer.parseInt(buffer.toString().trim()));
					else if(field.equals(MTEStruct.g_record_cnt))    trace.setRecordCount(Integer.parseInt(buffer.toString().trim()));
					else if(field.equals(MTEStruct.g_data_compress)) trace.setCompress(buffer.toString().trim());
				}
			}
			
			public void endElement(String uri, String localName, String qName) throws SAXException {
				
			}
		};
		parser.parse(is, dh);
		
		

	
		
		
		return trace;
	}

}
