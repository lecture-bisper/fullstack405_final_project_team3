import {useContext, useEffect, useState} from "react";
import UserContext from "../context/UserContext.jsx";
import CustomSidebar from "../component/sidebar/CustomSidebar.jsx";
import ProjectComponent from "../component/ProjectComponent.jsx";
import {useNavigate} from "react-router";

function ProjectManagement() {
    const { user } = useContext(UserContext);
    const navigate = useNavigate();

    // 진행, 완료, 모두보기
    const [isStats, setIsStats] = useState('all');
    const handleStatsBtn = (e) => {
        setIsStats(e);
    }

    // 로그인 정보 없을 시, 로그인 페이지로 이동
    useEffect(() => {
        if (!user || !user.id) {
            navigate('/');
        }
    }, [user, navigate]);

    return (
        <div className={'d-flex vh-100'}>
            <CustomSidebar topManager={user.topManager} />
            <div className={'flex-grow-1'}>
                <div className={'mainSection d-flex flex-column'}>
                    <div className={'managementSection'}>
                        <div className={'statsBtnSection mb-2'}>
                            <button
                                type={'button'}
                                value={'all'}
                                className={`statsBtn ${isStats === 'all' ? 'active' : ''}`}
                                onClick={() => handleStatsBtn('all')}
                            >
                                전체 보기
                            </button>
                            <button
                                type={'button'}
                                value={'in'}
                                className={`statsBtn ${isStats === 'in' ? 'active' : ''}`}
                                onClick={() => handleStatsBtn('in')}
                            >
                                진행 중
                            </button>
                            <button
                                type={'button'}
                                value={'out'}
                                className={`statsBtn ${isStats === 'out' ? 'active' : ''}`}
                                onClick={() => handleStatsBtn('out')}>
                                종료
                            </button>
                        </div>
                        <ProjectComponent
                            stats={isStats}
                            id={user.id}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
}

export default ProjectManagement;