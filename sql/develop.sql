select
	 max(tr.tracking_date)
	,min(tr.tracking_date)
  from top0503 tr
 where tr.tracking_date >= '20220422000000000000'
   and tr.tracking_date <= '20220422239999999999'
;

-- 10만건 중 100건 1초
select
	 *
  from top0503 tr
 where tr.tracking_date >= '20220427000000000000'
   and tr.tracking_date <= to_char(now(), 'yyyymmdd') || '999999999999'
order by tr.tracking_date desc
--OFFSET 0 limit 20 -- 오라클도 된다.
OFFSET 10 ROWS FETCH NEXT 10 ROWS only
;



select version();
--PostgreSQL 14.2 (Debian 14.2-1.pgdg110+1) on x86_64-pc-linux-gnu, compiled by gcc (Debian 10.2.1-6) 10.2.1 20210110, 64-bit
select
	*
  from top0503 tr
 where tr.tracking_date >= '20220427000000000000'
   and tr.tracking_date <= to_char(now(), 'yyyymmdd') || '999999999999'
order by tr.tracking_date desc
--OFFSET 0 limit 20; 오라클도 된다.
OFFSET 10 ROWS FETCH FIRST 10 ROWS only
; 

select
	*
  from top0503 tr
 where tr.tracking_date >= '20220427000000000000'
   and tr.tracking_date <= to_char(now(), 'yyyymmdd') || '999999999999'
   and tr.integration_id = 'TEST004'
order by tr.tracking_date desc
--OFFSET 0 limit 20; 오라클도 된다.
OFFSET 10 ROWS FETCH FIRST 10 ROWS only
; 


select to_char(now(), 'yyyymmddHH24miss')
;

-- 366816
select
	count(*)
  from top0503 tr
 where tr.tracking_date >= '20220427' || '000000000000'
   and tr.tracking_date <= to_char(now(), 'yyyymmdd') || '999999999999'
;

insert into tba0001 
select 
    '18' as job_id,
    grp_id,
    '시간별트레킹집계' as job_nm,
    'pep.per.mint.batch.job.su.TSU0803T9Job' as impl_class,
    type,
    table_nm,
    '시간별트레킹집계' as comments
  from tba0001
where job_id = '14'; 
commit;
select * from tba0001;

select * from tba0003;
select * from tba0004;
select * from tba0005 order by start_date desc;

insert into tba0003 values ('18', '시간별트레킹집계', 'Y');
commit;

insert into tba0004 values (
    '18', '18', '1', '0', '0', '0 0-59 * * * ?'
);

select * from tsu0302 where package='system'
and attribute_nm like '%batch%';

insert into tsu0302 values ('system', 'batch.run.standalone', 1, 'system.batch.run.standalone', TRUE, '독립실행여부', 'N', to_char(now(), 'yyyymmddHH24miss'), 'iip', '','')
;

select * from tsu0803;

select * from tsu0803;

SELECT
        interface_id								 as "interfaceId",
        substring(tracking_date, 1,10) 				 as "trDate",
        sum(data_amt) 								 as "dataSize",
        sum(case status when '00' then 1 else 0 end) as "successCnt",
        sum(case status when '99' then 1 else 0 end) as "errorCnt",
        sum(case status when '01' then 1 else 0 end) as "processCnt"
    FROM top0503
   where tracking_date between '2022052412' || '0000000000' and '2022052415' || '9999999999'
group by interface_id, substring(tracking_date, 1,10)



 