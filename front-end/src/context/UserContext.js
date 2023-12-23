import { createContext, useContext, useEffect, useState } from 'react';
import { getUserInfo } from '../api/UserApi';
import { useNavigate } from 'react-router-dom';
import { RoomContext } from './RoomContext';

export const UserContext = createContext();

export function UserContextProvider({ children }) {
    let navigate = useNavigate();
    const [auth, setAuth] = useState(false);
    const [user, setUser] = useState('');
    const [listRooms, setListRooms] = useState('');

    const { setContextRoomId, clearRoomContext } = useContext(RoomContext);

    const fetchUserData = async () => {
        // const token = localStorage.getItem('token');
        if (localStorage.getItem('token')) {
            let res = await getUserInfo();
            if (res && res.name && res.id && res.email) {
                setUser({ id: res.id, name: res.name, email: res.email });
                setListRooms(res.rooms);

                if (localStorage.getItem('roomId')) {
                    if (res.rooms.some((room) => room.roomId == localStorage.getItem('roomId'))) {
                        setContextRoomId(localStorage.getItem('roomId'));
                        navigate('/expenses');
                    } else {
                        localStorage.removeItem('roomId');
                        if (!listRooms) {
                            //navigate based on list rooms of user, only do when login or reload
                            if (res.rooms.length > 0) {
                                setContextRoomId(res.rooms[0].roomId);
                                navigate('/expenses');
                            } else {
                                navigate('/');
                            }
                        }
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

    return (
        <UserContext.Provider value={{ user, listRooms, setContextLoginRegister, clearUserContext, fetchUserData }}>
            {children}
        </UserContext.Provider>
    );
}

export function getUserIdFromUserContext(user) {
    return user.id;
}
