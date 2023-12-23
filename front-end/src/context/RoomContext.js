import { createContext, useContext, useEffect, useState } from 'react';
import { getRoomData } from '../api/RoomApi';
import { useNavigate } from 'react-router-dom';
import { UserContext, getUserIdFromUserContext } from './UserContext';

export const RoomContext = createContext();

export function RoomContextProvider({ children }) {
    const [roomId, setRoomId] = useState('');
    const [listExpenses, setListExpenses] = useState([]);
    const [listMembers, setListMembers] = useState([]);
    const [roomInfo, setRoomInfo] = useState('');

    const setContextRoomId = (id) => {
        setRoomId(id);
    };

    const clearRoomContext = () => {
        setRoomId('');
        setListExpenses([]);
        setListMembers([]);
        setRoomInfo('');
        localStorage.removeItem('roomId');
    };

    useEffect(() => {
        if (roomId) {
            fetchRoomData();
        }
    }, [roomId]);

    const fetchRoomData = async () => {
        let res = await getRoomData(roomId);
        if (res && res.roomId) {
            setListMembers(res.members);
            setListExpenses(res.expenses);
            setRoomInfo({
                roomName: res.roomName,
                owner: res.owner,
                totalAmount: res.totalSpending,
                memberCount: res.memberCount,
                ownerId: res.ownerId,
            });
            localStorage.setItem('roomId', roomId);
        }
    };

    return (
        <RoomContext.Provider
            value={{ roomId, roomInfo, listExpenses, listMembers, setContextRoomId, clearRoomContext, fetchRoomData }}
        >
            {children}
        </RoomContext.Provider>
    );
}
