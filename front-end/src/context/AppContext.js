import { createContext, useContext, useEffect, useState } from 'react';
import { getUserInfo } from '../api/UserApi';
import { useNavigate } from 'react-router-dom';
import { RoomContext } from './RoomContext';
import { getRoomData } from '../api/RoomApi';

export const AppContext = createContext();

export function AppContextProvider({ children }) {
    let navigate = useNavigate();

    //USER CONTEXT
    const [auth, setAuth] = useState(false);
    const [user, setUser] = useState('');
    const [listRooms, setListRooms] = useState('');

    const fetchUserData = async () => {
        // const token = localStorage.getItem('token');
        if (localStorage.getItem('token')) {
            let res = await getUserInfo();
            if (res && res.name && res.id && res.email) {
                setUser({ id: res.id, name: res.name, email: res.email });
                setListRooms(res.rooms);

                // if (localStorage.getItem('roomId')) {
                //     if (res.rooms.some((room) => room.roomId == localStorage.getItem('roomId'))) {
                //         setContextRoomId(localStorage.getItem('roomId'));
                //         navigate('/expenses');
                //     } else {
                //         localStorage.removeItem('roomId');
                //         if (!listRooms) {
                //             //navigate based on list rooms of user, only do when login or reload
                //             if (res.rooms.length > 0) {
                //                 setContextRoomId(res.rooms[0].roomId);
                //                 navigate('/expenses');
                //             } else {
                //                 navigate('/');
                //             }
                //         }
                //     }
                // }
                //navigate based on list rooms of user, only do when login or reload
                if (!listRooms) {
                    if (res.rooms.length > 0) {
                        setContextRoomId(res.rooms[0].roomId);
                        navigate('/expenses');
                    } else {
                        navigate('/');
                    }
                }
            }
        }
        if (!localStorage.getItem('token')) {
            clearUserContext();
        }
    };

    //fetch user data when auth status changes
    useEffect(() => {
        fetchUserData();
    }, [auth]);

    const setContextLoginRegister = (token) => {
        localStorage.setItem('token', token);
        setAuth(true);
    };

    const clearUserContext = () => {
        localStorage.removeItem('token');
        navigate('/login');
        setAuth(false);
        setUser('');
        setListRooms('');
        clearRoomContext();
    };

    //ROOM CONTEXT
    const [roomId, setRoomId] = useState('');
    const [isRoomOwner, setIsRoomOwner] = useState(false);
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
        // localStorage.removeItem('roomId');
    };

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
            setIsRoomOwner(res.ownerId === user.id);
            // localStorage.setItem('roomId', roomId);
        }
    };

    useEffect(() => {
        if (roomId) {
            fetchRoomData();
        }
    }, [roomId]);

    return (
        <AppContext.Provider
            value={{
                user,
                listRooms,
                setContextLoginRegister,
                clearUserContext,
                fetchUserData,
                roomId,
                roomInfo,
                listExpenses,
                listMembers,
                isRoomOwner,
                setContextRoomId,
                clearRoomContext,
                fetchRoomData,
            }}
        >
            {children}
        </AppContext.Provider>
    );
}
