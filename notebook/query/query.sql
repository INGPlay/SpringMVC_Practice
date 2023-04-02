-- conn system/?;

CREATE USER notebook IDENTIFIED BY "notebook";

GRANT CONNECT,RESOURCE TO notebook; 

-- conn notebook/?;

-- table 생성
CREATE TABLE container_tbl (
	c_id number,
	c_title varchar(50) NOT NULL,
	CONSTRAINT container_pk PRIMARY KEY (c_id)
);

CREATE TABLE post_tbl (
	c_id number,
	p_id number,
	p_title varchar(100) NOT NULL,
	p_content varchar(1000) NOT NULL,
	p_created date NOT NULL,
	p_updated date NOT NULL,
	CONSTRAINT post_pk PRIMARY KEY (c_id, p_id),
	CONSTRAINT post_fk FOREIGN KEY (c_id) REFERENCES container_tbl (c_id) ON DELETE CASCADE
);
--

-- table 삭제
DELETE FROM CONTAINER_TBL ct;

--

-- Sequence 생성 및 삭제
CREATE SEQUENCE c_id_seq  
    START WITH 1 
    INCREMENT BY 1 
    MINVALUE 1   
    NOCYCLE;
 
BEGIN
  EXECUTE IMMEDIATE 'DROP SEQUENCE ' || 'c_id_seq';
EXCEPTION
  WHEN OTHERS THEN
    IF SQLCODE != -2289 THEN
      RAISE;
    END IF;
END;

--

SELECT * FROM container_tbl LEFT JOIN post_tbl
ON post_tbl.c_id = container_tbl.c_id;

-- test
INSERT INTO container_tbl (c_id, c_title) VALUES (c_id_seq.nextval, '아무거나');
INSERT INTO container_tbl (c_id, c_title) VALUES (c_id_seq.nextval, '두번째');
INSERT INTO container_tbl (c_id, c_title) VALUES (c_id_seq.nextval, '세번째');

INSERT INTO post_tbl (c_id, p_id, p_title, p_content, p_created, p_updated)
VALUES (1, (SELECT count(p_id) + 1 FROM post_tbl WHERE c_id = 1) , 'test1', '흠 잘모르겠네요', sysdate, sysdate); 

INSERT INTO post_tbl (c_id, p_id, p_title, p_content, p_created, p_updated)
VALUES (2, (SELECT count(p_id) + 1 FROM post_tbl WHERE c_id = 2) , 'test2', '흠 잘모르겠네요', sysdate, sysdate);

DELETE FROM container_tbl;

SELECT * FROM container_tbl;

SELECT USER FROM DUAL;

SELECT to_char(SYSDATE, 'YYYY-MM-DD HH24:MI:SS') FROM dual;

UPDATE CONTAINER_TBL SET c_title = '바뀐 두번째' WHERE c_id = 2;

SELECT * FROM POST_TBL pt WHERE c_id = 2 AND p_id = 2;

UPDATE POST_TBL  SET p_title = '수정 수정', p_content = '된 컨텐츠입니다', p_updated = sysdate
    WHERE c_id = 2 and p_id = 2;
    
SELECT c_id_seq.currval FROM dual;

SELECT CONTAINER_TBL SET c_title = #{title}
                     WHERE c_id = ${id}