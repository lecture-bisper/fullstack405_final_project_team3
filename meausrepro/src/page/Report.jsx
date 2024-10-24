import {useContext, useEffect, useState} from "react";
import UserContext from "../context/UserContext.jsx";
import CustomSidebar from "../component/sidebar/CustomSidebar.jsx";
import axios from "axios";
import {useNavigate} from "react-router";
import ReportComponent from "../component/ReportComponent.jsx";

function Report() {
    const { user } = useContext(UserContext);
    const navigate = useNavigate();

    // 프로젝트 선택
    const [isProject, setIsProject] = useState();
    const handleProjectClick = (project) => {
        if (project != null) {
            setIsProject(project);
            setIsSection(null); // 섹션을 초기 상태로 리셋
            handleSectionList(project);
        }
    }

    // 구간 선택
    const [isSection, setIsSection] = useState();
    const handleSectionClick = (section) => {
        if (section != null) {
            setIsSection(section);
        }
    }

    // 프로젝트 리스트 가져오기
    const [projectList, setProjectList] = useState([]);
    const fetchProjects = () => {
        axios.get(`http://localhost:8080/MeausrePro/Project/inProgress/${encodeURIComponent(user.id)}`)
            .then(res => {
                setProjectList(res.data);
            })
            .catch(err => {
                console.log(err);
            });
    };

    // 프로젝트 전체 구간 들고오기
    const [sectionList, setSectionList] = useState([]);
    const handleSectionList = (projectId) => {
        axios.get(`http://localhost:8080/MeausrePro/Section/${projectId}`)
            .then((res) => {
                setSectionList(res.data);
            })
            .catch(err => {
                console.log(err);
            });
    };

    // 로그인 정보 없을 시, 로그인 페이지로 이동
    useEffect(() => {
        if (!user || !user.id) {
            navigate('/');
        }
        fetchProjects();
    }, [user, navigate]);

    return (
        <div className={'d-flex vh-100'}>
            <CustomSidebar topManager={user.topManager} />
            <div className={'flex-grow-1'}>
                <div className={'mainSection d-flex flex-column'}>
                    <div className={'managementSection'}>
                        <div className={'statsBtnSection gap-2 mb-2'}>
                            <select
                                className={'reportSelect'}
                                onChange={(e) => handleProjectClick(e.target.value)}
                                id={'projectGroup'}>
                                <option value="" selected>선택하세요</option>
                                {projectList.map((item) => {
                                    return (
                                        <option value={item.idx} key={item.idx}>
                                            {item.siteName}
                                        </option>
                                    )
                                })}
                            </select>
                            <select
                                className={'reportSelect'}
                                onChange={(e) => handleSectionClick(e.target.value)}
                                id={'sectionGroup'}>
                                <option value="" selected>선택하세요</option>
                                {sectionList.length > 0 && (
                                    sectionList.map((item) => {
                                        return (
                                            <option value={item.idx} key={item.idx}>
                                                {item.sectionName}
                                            </option>
                                        )
                                    })
                                )}
                            </select>
                        </div>
                        <ReportComponent
                            sectionIdx={isSection}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Report;
