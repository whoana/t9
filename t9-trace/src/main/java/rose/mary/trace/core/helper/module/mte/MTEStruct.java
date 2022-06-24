/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.helper.module.mte;



/**
 * <pre>
 * rose.mary.trace.helper.module
 * RFH2Structure.java
 * </pre>
 * @author whoana
 * @date Jul 23, 2019
 */
public class MTEStruct {
	
	//--------------------------------------------------------------------
	public final static String mcd = "mcd";	
	//--------------------------------------------------------------------
	public final static String msd = "Msd"; 
	public final static String set = "Set"; 
	public final static String type = "Type"; 
	public final static String fmt = "Fmt"; 
	
	public final static String key_mcd_msd  = "mcd.Msd"; 
	public final static String key_mcd_set  = "mcd.Set"; 
	public final static String key_mcd_type = "mcd.Type"; 
	public final static String key_mcd_fmt  = "mcd.Fmt"; 
	
	
	
	public final static String usr = "usr";
	public final static String mte_info = "mte_info";
	
	//--------------------------------------------------------------------
	public final static String a = "interface_info";
	//--------------------------------------------------------------------
	public final static String a_host_id   = "host_id";
	public final static String a_group_id  = "group_id";
	public final static String a_intf_id   = "intf_id";
	public final static String a_date 	   = "date";
	public final static String a_time 	   = "time";
	public final static String a_global_id = "global_id"; 
	
	
	//--------------------------------------------------------------------
	public final static String b = "host_info";
	//--------------------------------------------------------------------
	public final static String b_host_id 	= "host_id";
	public final static String b_os_type 	= "os_type";
	public final static String b_os_version = "os_version";
	 
	
	//--------------------------------------------------------------------
	public final static String c = "process_info";
	//--------------------------------------------------------------------
	public final static String c_date 		    = "date";
	public final static String c_time 		    = "time";
	public final static String c_process_mode   = "process_mode";
	public final static String c_process_type   = "process_type";
	public final static String c_process_id     = "process_id";
	public final static String c_hub_cnt 	    = "hub_cnt";
	public final static String c_spoke_cnt 	    = "spoke_cnt";
	public final static String c_recv_spoke_cnt = "recv_spoke_cnt";
	public final static String c_hop_cnt 	    = "hop_cnt";
	public final static String c_appl_type 	    = "appl_type";
	public final static String c_timezone 	    = "timezone";
	public final static String c_elaspsed_time 	= "elaspsed_time";
	
	//--------------------------------------------------------------------
	public final static String d = "status_info";
	//--------------------------------------------------------------------
	public final static String d_status 	   = "status";
	public final static String d_error_type    = "error_type";
	public final static String d_error_code    = "error_code";
	public final static String d_error_reason  = "error_reason";
	public final static String d_error_message = "error_message";
	public final static String d_errorq_msgid  = "errorq_msgid";
	public final static String d_targetq 	   = "targetq";

	//--------------------------------------------------------------------
	public final static String e = "sender_info";
	//--------------------------------------------------------------------
	public final static String e_replytoqmgr = "replytoqmgr";
	public final static String e_replytoq	 = "replytoq";
	public final static String e_file_name   = "file_name";
	public final static String e_directory   = "directory";
	public final static String e_extract_pgm = "extract_pgm";
	public final static String e_description = "description";
	
	//--------------------------------------------------------------------
	public final static String f = "receiver_info";
	//--------------------------------------------------------------------
	public final static String f_host_id 	 = "host_id";
	public final static String f_directory 	 = "directory";
	public final static String f_file_name 	 = "file_name";
	public final static String f_upload_pgm = "upload_pgm";

	//--------------------------------------------------------------------
	public final static String g = "data_info";
	//--------------------------------------------------------------------
	public final static String g_record_cnt 		= "record_cnt";
	public final static String g_record_size 		= "record_size";
	public final static String g_data_size 			= "data_size";
	public final static String g_data_compress 		= "data_compress";
	public final static String g_compression_method = "compression_method";
	public final static String g_compression_mode 	= "compression_mode";
	public final static String g_compression_size 	= "compression_size";
	public final static String g_data_conversion 	= "data_conversion";
	public final static String g_converted_size 	= "converted_size";
	public final static String g_conv_mode 			= "conv_mode";

	//--------------------------------------------------------------------
	public final static String h = "data_key_info"; 
	//--------------------------------------------------------------------
	
	//--------------------------------------------------------------------
	public final static String i = "custom_info"; 
	//--------------------------------------------------------------------
	public final static String i_master01 = "master01";
	public final static String i_master02 = "master02";
	public final static String i_master03 = "master03";
	public final static String i_master04 = "master04";
	public final static String i_master05 = "master05";
	public final static String i_detail01 = "detail01";
	public final static String i_detail02 = "detail02";
	public final static String i_detail03 = "detail03";
	public final static String i_detail04 = "detail04";
	public final static String i_detail05 = "detail05";
		
	
	//--------------------------------------------------------------------
	public final static String j = "prev_host_info";
	//--------------------------------------------------------------------
	public final static String j_host_id 	= "host_id";
	public final static String j_process_id = "process_id ";
	
	
	
	public final static String COMPRESS_Y = "Y";
	public final static String COMPRESS_N = "N";

	public final static String STATUS_FINISHED = "00";
	public final static String STATUS_PROCESSING  = "01";
	public final static String STATUS_ERROR  = "99";
	
	public final static String PROCESS_MODE_SNDR = "SNDR";
	public final static String PROCESS_MODE_BRKR = "BRKR";
	public final static String PROCESS_MODE_RCVR = "RCVR";
	public final static String PROCESS_MODE_REPL = "REPL";
 
	
}