-----------------------------------------------
-- 2 프로세스 스레드 8 : 
-----------------------------------------------
SELECT round(a.cnt/a.dur) AS T1 
FROM (
	SELECT count(*) AS cnt , 
		round(
			(TO_DATE(SUBSTR(max(REG_DATE),0,14),'yyyymmddhh24miss') - TO_DATE(SUBSTR(min(REG_DATE),0,14),'yyyymmddhh24miss')
			) * 24 * 60 * 60) AS dur
	FROM TOP0501 WHERE REG_DATE >= '20201021143000000000' AND REG_DATE  <= '20201021145900000000'
) a
UNION all
SELECT round(a.cnt/a.dur) AS T3 
FROM (
	SELECT count(*) AS cnt , 
		round(
			(TO_DATE(SUBSTR(max(REG_DATE),0,14),'yyyymmddhh24miss') - TO_DATE(SUBSTR(min(REG_DATE),0,14),'yyyymmddhh24miss')
			) * 24 * 60 * 60) AS dur
	FROM TOP0503 WHERE REG_DATE >= '20201021143000000000' AND REG_DATE  <= '20201021145900000000'
) a;



-----------------------------------------------
-- 2 프로세스 스레드 8 : 
-----------------------------------------------
INSERT INTO TOP0507
SELECT to_char(sysdate,'yyyymmddhh24miss'), 't1', round(a.cnt/a.dur) AS tps, cnt , etime, stime
FROM (
	SELECT count(*) AS cnt , 
		SUBSTR(max(REG_DATE),0,14)  AS etime,
		SUBSTR(min(REG_DATE),0,14)  AS stime,
		round(
			(TO_DATE(SUBSTR(max(REG_DATE),0,14),'yyyymmddhh24miss') - TO_DATE(SUBSTR(min(REG_DATE),0,14),'yyyymmddhh24miss')
			) * 24 * 60 * 60) AS dur
	FROM TOP0501 WHERE REG_DATE >= '20201021154500000000' AND REG_DATE  <= '20201021155000000000'
) a
UNION all
SELECT to_char(sysdate,'yyyymmddhh24miss'), 't3', round(a.cnt/a.dur) AS tps, cnt , etime, stime
FROM (
	SELECT count(*) AS cnt ,  
		SUBSTR(max(REG_DATE),0,14)  AS etime,
		SUBSTR(min(REG_DATE),0,14)  AS stime,
		round(
			(TO_DATE(SUBSTR(max(REG_DATE),0,14),'yyyymmddhh24miss') - TO_DATE(SUBSTR(min(REG_DATE),0,14),'yyyymmddhh24miss')
			) * 24 * 60 * 60) AS dur
	FROM TOP0503 WHERE REG_DATE >= '20201021154500000000' AND REG_DATE  <= '20201021155000000000'
) a;


SELECT  * FROM TOP0507 ORDER BY 1 desc;




SELECT * FROM tsu0302;


SELECT * FROM tim0002;

SELECT * FROM tan0502;

