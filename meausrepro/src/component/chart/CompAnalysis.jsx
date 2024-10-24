import { useContext, useEffect, useState } from "react";
import {useNavigate, useParams} from "react-router";
import axios from "axios";
import UserContext from "../../context/UserContext.jsx";
import CustomSidebar from "../sidebar/CustomSidebar.jsx";
import StackedLineChart from "./StackedLineChart.jsx";

const CompAnalysis = () => {
    const { sectionId } = useParams();  // URL에서 섹션 ID를 가져옴
    const { user } = useContext(UserContext);
    const [deviceList, setDeviceList] = useState([]);
    const [measurementsList, setMeasurementsList] = useState({});

    const navigate = useNavigate();

    // 계측기 리스트 불러오기
    useEffect(() => {
        axios.get(`http://localhost:8080/MeausrePro/Instrument/section/${sectionId}`)
            .then(response => {
                setDeviceList(response.data);
            })
            .catch(err => {
                console.error("계측기 리스트 조회 중 오류 발생", err);
            });
    }, [sectionId]);

    // 각 계측기의 측정 데이터 불러오기
    useEffect(() => {
        if (deviceList.length > 0) {
            deviceList.forEach((device) => {
                axios.get(`http://localhost:8080/MeausrePro/Management/details/${device.idx}`)  // 각 계측기의 ID로 요청
                    .then((response) => {
                        console.log("측정 데이터 응답:", response.data);
                        setMeasurementsList((prev) => ({
                            ...prev,
                            [device.idx]: response.data.map((item) => ({
                                date: item.management?.createDate || '',  // 빈 문자열로 초기화
                                gage1: item.managementType?.gage1 || 0,
                                gage2: item.managementType?.gage2 || 0,
                                gage3: item.managementType?.gage3 || 0,
                                gage4: item.managementType?.gage4 || 0,
                                crackWidth: item.management?.crackWidth || 0,
                            })),
                        }));
                    })
                    .catch((err) => {
                        console.error("측정 데이터 조회 중 오류 발생", err);
                    });
            });
        }
    }, [deviceList]);

    // 로그인 정보 없을 시, 로그인 페이지로 이동
    useEffect(() => {
        if (!user || !user.id) {
            navigate('/');
        }
    }, [user, navigate]);

    return (
        <div className="d-flex vh-100" style={{overflow: 'hidden'}}>
            <CustomSidebar topManager={user.topManager} />
            <div className="flex-grow-1" style={{overflowY: 'auto'}}>
                {deviceList.length > 0 ? (
                    <div className="comp-chart">
                        {deviceList.map(device => (
                            <div key={device.idx} className="border m-1">
                                <p className='comp-name text-center'>{device.insName}</p>
                                <StackedLineChart
                                    data={measurementsList[device.idx] || []}
                                    instrumentType={device.insType}
                                />

                            </div>
                        ))}
                    </div>
                ) : (
                    <h3>구간에 계측기가 없습니다.</h3>
                )}
            </div>
        </div>
    );
};

export default CompAnalysis;
