import {useEffect, useState} from "react";
import Pagination from "./pagination/Pagination.jsx";
import axios from "axios";
import Swal from "sweetalert2";

function ReportComponent(props) {
    const { sectionIdx } = props;

    const [reportList, setReportList] = useState([]);
    // 리포트 리스트 불러오기
    const handleReportList = () => {
        if (sectionIdx && sectionIdx !== "") {
            axios.get(`http://localhost:8080/MeausrePro/report/reports/${sectionIdx}`)
                .then(res => {
                    setReportList(res.data);
                })
                .catch(error => {
                    Swal.fire({
                        icon:"error",
                        text:"리포트를 불러오지 못했습니다.",
                        showCancelButton: false,
                        confirmButtonText: '확인'
                    })
                    console.log(error);
                });
        } else {
            setReportList([]);
        }
    }

    // 리포트 삭제
    const handleReportDelete = (report) => {
        axios.delete(`http://localhost:8080/MeausrePro/report/delete/${report.idx}`)
            .then(() => {
                handleReportList();
            })
            .catch(err => {
                console.log(err);
            });
    }

    // 현재 페이지 번호
    const [page, setPage] = useState(1);
    // 페이지 당 게시글 수
    const itemsPerPage = 15;

    // 페이지 이동
    const changePageHandler = (page) => {
        setPage(page);
    };
    // 페이지네이션을 통해 보여줄 slice된 리스트
    const [currentList, setCurrentList] = useState(reportList);

    const indexOfLastItem = page * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;

    useEffect(() => {
        handleReportList()
    }, [sectionIdx])

    useEffect(() => {
        setCurrentList(reportList.slice(indexOfFirstItem, indexOfLastItem));
    }, [page, reportList]);

    return (
        <div className={'paginationSection'}>
            <table className={'table text-center'} style={{verticalAlign: 'middle'}}>
                <thead>
                <tr>
                    <th>파일 이름</th>
                    <th>업로드 날짜</th>
                    <th>삭제</th>
                </tr>
                </thead>
                <tbody>
                {currentList.length > 0 ?(
                    currentList.map((report, index) => (
                        <tr key={index}>
                            <td>{report.fileName}</td>
                            <td>{new Date(report.uploadDate).toLocaleString('ko-KR', {
                                year: 'numeric',
                                month: '2-digit',
                                day: '2-digit',
                                hour: '2-digit',
                                minute: '2-digit'
                            })}</td>
                            <td>
                                <button
                                    type={'button'}
                                    onClick={() => handleReportDelete(report)}
                                    className={'iconBtn iconBtnRed'}
                                >
                                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20"
                                         fill="currentColor"
                                         className="bi bi-trash3" viewBox="0 0 16 16">
                                        <path
                                            d="M6.5 1h3a.5.5 0 0 1 .5.5v1H6v-1a.5.5 0 0 1 .5-.5M11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3A1.5 1.5 0 0 0 5 1.5v1H1.5a.5.5 0 0 0 0 1h.538l.853 10.66A2 2 0 0 0 4.885 16h6.23a2 2 0 0 0 1.994-1.84l.853-10.66h.538a.5.5 0 0 0 0-1zm1.958 1-.846 10.58a1 1 0 0 1-.997.92h-6.23a1 1 0 0 1-.997-.92L3.042 3.5zm-7.487 1a.5.5 0 0 1 .528.47l.5 8.5a.5.5 0 0 1-.998.06L5 5.03a.5.5 0 0 1 .47-.53Zm5.058 0a.5.5 0 0 1 .47.53l-.5 8.5a.5.5 0 1 1-.998-.06l.5-8.5a.5.5 0 0 1 .528-.47M8 4.5a.5.5 0 0 1 .5.5v8.5a.5.5 0 0 1-1 0V5a.5.5 0 0 1 .5-.5"/>
                                    </svg>
                                </button>
                            </td>
                        </tr>
                    ))
                ) : (
                    <tr>
                        <td colSpan={3} className={'text-center'}>
                            <span>작성 된 구간 리포트가 없습니다.</span>
                        </td>
                    </tr>
                )}
                </tbody>
            </table>
            {reportList.length > 0 && (
                <Pagination
                    activePage={page}
                    itemsCountPerPage={itemsPerPage}
                    totalItemsCount={reportList.length}
                    onChange={changePageHandler}
                />
            )}
        </div>
    );
}

export default ReportComponent;