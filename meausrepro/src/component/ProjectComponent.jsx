import {useEffect, useState} from "react";
import axios from "axios";
import Pagination from "../component/pagination/Pagination.jsx";

function ProjectComponent(props) {
    const { stats, id } = props;

    const [projectList, setProjectList] = useState([]);
    const handleProjectList = () => {
        axios.get(`http://localhost:8080/MeausrePro/Project/${id}/${stats}`)
            .then(res => {
                const data = res.data;
                console.log(data);
                setProjectList(data);
            })
            .catch(err => {
                console.log(err);
            })
    }

    // 프로젝트 진행 여부 수정
    const handleProjectStats = (project) => {
        const updatedStats = project.siteCheck === 'Y' ? 'N' : 'Y';  // Y -> N 또는 N -> Y

        // UI 먼저 업데이트
        setProjectList(prevList =>
            prevList.map(p => p.idx === project.idx ? { ...p, siteCheck: updatedStats } : p)
        );

        axios.put(`http://localhost:8080/MeausrePro/Project/update/${project.idx}/${updatedStats}`)
            .then(() => {
                setTimeout(() => {
                    handleProjectList();
                }, 500);
            })
            .catch(err => {
                console.log(err);
            });
    }

    // 프로젝트 삭제
    const handleProjectDelete = (project) => {
        axios.delete(`http://localhost:8080/MeausrePro/Project/delete/${project.idx}`)
            .then(() => {
                handleProjectList();
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
    const [currentList, setCurrentList] = useState(projectList);

    const indexOfLastItem = page * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;

    useEffect(() => {
        handleProjectList();
    }, [stats])

    useEffect(() => {
        setCurrentList(projectList.slice(indexOfFirstItem, indexOfLastItem));
    }, [page, projectList]);

    return (
        <div className={'paginationSection'}>
            <table className={'table text-center'} style={{verticalAlign: 'middle'}}>
                <thead>
                <tr>
                    <th>프로젝트명</th>
                    <th>회사명</th>
                    <th>과업기간</th>
                    <th>진행여부</th>
                    <th>삭제</th>
                </tr>
                </thead>
                <tbody>
                {currentList.length > 0 ? (
                    currentList.map((project, index) => (
                        <tr key={index}>
                            <td>{project.siteName}</td>
                            <td>{project.companyIdx?.companyName || '회사 정보 없음'}</td>
                            <td>
                                {project.startDate} ~ {project.endDate}
                            </td>
                            <td>
                                <div className={'form-switch'}>
                                    <input
                                        type={'checkbox'}
                                        className={'form-check-input custom-switch'}
                                        role={'switch'}
                                        id={`siteCheck${index}`}
                                        checked={project.siteCheck === 'N'}
                                        onChange={() => handleProjectStats(project)}
                                    />
                                </div>
                            </td>
                            <td>
                                <button
                                    type={'button'}
                                    onClick={() => handleProjectDelete(project)}
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
                        <td colSpan={5} className={'text-center'}>
                            {stats === 'in' && (
                                <span>진행 중인 프로젝트가 없습니다.</span>
                            )}
                            {stats === 'out' && (
                                <span>종료된 프로젝트가 없습니다.</span>
                            )}
                            {stats === 'all' && (
                                <span>프로젝트가 없습니다.</span>
                            )}
                        </td>
                    </tr>
                )}
                </tbody>
            </table>
            {projectList.length > 0 && (
                <Pagination
                    activePage={page}
                    itemsCountPerPage={itemsPerPage}
                    totalItemsCount={projectList.length}
                    onChange={changePageHandler}
                />
            )}
        </div>
    );
}

export default ProjectComponent;