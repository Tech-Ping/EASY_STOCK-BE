-- STOCK 테이블 생성
CREATE TABLE IF NOT EXISTS stock (
                                     stock_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR(255) NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    type VARCHAR(50) NOT NULL,
    info TEXT,
    img_url VARCHAR(255)
    );

-- STOCK 데이터 삽입
INSERT INTO stock (name, code, type, info, img_url) VALUES
                                                        ('삼성전자', '005930', 'KOSPI', '증30 담140 신10억', 'https://file.alphasquare.co.kr/media/images/stock_logo/kr/005930.png'),
                                                        ('LG에너지솔루션', '373220', 'KOSPI', '증30 담140 신10억', 'https://cdn.hankyung.com/photo/202410/01.37802553.1.jpg'),
                                                        ('비에이치아이', '083650', 'KOSDAQ', '증100', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQcmDfqjVGtBTlI5CrfvYHrDCn1ZAPffv6GZQ&s'),
                                                        ('현대차', '005380', 'KOSPI', '증30 담140 신10억', 'https://i.namu.wiki/i/Iu9zZQJ6VDhoEC3goMITaHnBNOAbxz5buSqhkMwnNcjBIvbURav08bsIPoY9pn7EBu-Hqlp-x94o6rD8J3vJ2g.svg'),
                                                        ('카카오', '035720', 'KOSDAQ', '증30 담140 신10억', 'https://i.namu.wiki/i/GmQozcg0lMGkI_NXkm04l-14hJIGnxYdhfe98DUlKGHVunjQtRkn0ZaGgXI1DEdGzHCzUsJsLbEZlMveOEnoRQ.svg'),
                                                        ('네이버', '035420', 'KOSPI', '증30 담140 신10억', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQXAaHnq8yZLx35RtNPQRemdKbT1UrmsG1IYg&s'),
                                                        ('SK하이닉스', '000660', 'KOSPI', '증30 담140 신10억', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSHO7HxKI-F9x3xOb2Hy4OhR653rqUrrXjNUw&s'),
                                                        ('포스코퓨처엠', '003670', 'KOSPI', '증30 담140 신10억', 'https://i.namu.wiki/i/iOVo3YA_QDJR4BnKl9i90J1Xef9yBDbcDUVwGA750zuPFMfcAvsV0LsbyWk-a7UOixtnlpvw8GzU9KywUsyVJA.svg');


