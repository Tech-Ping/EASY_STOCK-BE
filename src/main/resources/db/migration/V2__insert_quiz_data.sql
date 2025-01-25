CREATE TABLE IF NOT EXISTS quiz (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    question VARCHAR(255) NOT NULL,
    answer_index INT NOT NULL,
    level VARCHAR(20) NOT NULL
    );

CREATE TABLE IF NOT EXISTS quiz_options (
                                            quiz_id BIGINT NOT NULL,
                                            options VARCHAR(255) NOT NULL,
    FOREIGN KEY (quiz_id) REFERENCES quiz(id)
    );

-- QUIZ 데이터 삽입
INSERT INTO QUIZ (id, question, answer_index, level) VALUES
                                                         (1, 'KOSPI와 KOSDAQ의 차이점은?', 0, 'ZERO'),
                                                         (2, '예수금과 미수금의 차이점은?', 1, 'ZERO'),
                                                         (3, '종가 가지고 등락률 계산하기 문제: 전일 종가가 1000원이고, 현재가가 1200원일 때 등락률은?', 2, 'ZERO'),
                                                         (4, '매입가와 현재가로 수익금을 계산하는 문제: 매입가가 5000원, 현재가가 7000원일 때 수익금은?', 1, 'ZERO'),
                                                         (5, '왜 미체결 주문이 발생할까요?', 0, 'ZERO'),
                                                         (6, '주식 창에서 무엇이 무엇인지 맞추는 문제: 보유 주식 정보는 어디에 표시되나요?', 1, 'ONE'),
                                                         (7, '호가와 주문 잔량에 대한 설명: 호가는 무엇을 의미하나요?', 0, 'ONE'),
                                                         (8, '매도와 매수의 차이점은 무엇인가요?', 0, 'ONE'),
                                                         (9, '시장가 주문과 지정가 주문의 차이점은 무엇인가요?', 0, 'ONE');

-- QUIZ_OPTIONS 데이터 삽입
INSERT INTO QUIZ_OPTIONS (quiz_id, options) VALUES
                                                (1, 'KOSPI는 대형주, KOSDAQ은 중소형주 중심입니다.'),
                                                (1, 'KOSPI는 외환 거래소입니다.'),
                                                (1, 'KOSDAQ은 선물 시장입니다.'),
                                                (2, '예수금은 수익금의 다른 말입니다.'),
                                                (2, '예수금은 투자 가능한 금액, 미수금은 미납 대금입니다.'),
                                                (2, '미수금은 해외 주식에만 적용됩니다.'),
                                                (3, '10%'),
                                                (3, '15%'),
                                                (3, '20%'),
                                                (4, '3000원'),
                                                (4, '2000원'),
                                                (4, '1000원'),
                                                (5, '지정가가 현재 시장가보다 낮거나 높아서'),
                                                (5, '주문량이 부족해서'),
                                                (5, '시장이 닫혀 있어서'),
                                                (6, '좌측 사이드 메뉴'),
                                                (6, '상단 탭'),
                                                (6, '하단 탭'),
                                                (7, '주식 매수/매도를 위한 가격'),
                                                (7, '주식이 체결된 수량'),
                                                (7, '주식 시장에서 제공되는 총 잔량'),
                                                (8, '매도는 주식을 파는 것, 매수는 주식을 사는 것'),
                                                (8, '매수는 주식을 판매하는 것'),
                                                (8, '매도는 주식을 구매하는 것'),
                                                (9, '시장가는 즉시 체결, 지정가는 원하는 가격에서 체결'),
                                                (9, '지정가는 예약 주문 방식'),
                                                (9, '시장가는 즉시 체결 방식');
