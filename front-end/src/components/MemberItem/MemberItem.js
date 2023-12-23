import { useContext, useEffect, useState } from 'react';
import './MemberItem.scss';
import Button from 'react-bootstrap/Button';
import { RoomContext } from '../../context/RoomContext';
import { changeRoomOwner, removeMember } from '../../api/RoomApi';
import { toast } from 'react-toastify';
import { UserContext } from '../../context/UserContext';
import { AppContext } from '../../context/AppContext';

function MemberItem(props) {
    const { userId, name, balance, role, totalSpent, status } = props;
    const [option, setOption] = useState('');
    useEffect(() => {
        setOption(status);
    }, [status]);

    const { roomId, roomInfo, isRoomOwner, fetchRoomData, user } = useContext(AppContext);

    return (
        <div className="member-item">
            <div className="icon">
                <i className="fa-solid fa-user"></i>
            </div>

            <div className="info">
                <table>
                    <tbody>
                        <tr>
                            <td className="col-1">
                                <strong>{name}</strong>
                            </td>
                            <td className="col-2">
                                <span className="category">Balance:</span>
                                <strong>
                                    <span className="amount">{balance}</span>
                                    <span>USD</span>
                                </strong>
                            </td>
                        </tr>
                        <tr>
                            <td className="col-1">
                                <span className="category">Role:</span>
                                <strong>{role}</strong>
                            </td>
                            <td className="col-2">
                                <span className="category">Total spent:</span>
                                <strong>
                                    <span className="amount">{totalSpent}</span>
                                    <span>USD</span>
                                </strong>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>

            {isRoomOwner && role == 'MEMBER' && (
                <>
                    {option == 'removeMember' && (
                        <div
                            className="button"
                            onClick={async () => {
                                let res = await removeMember(roomId, userId);
                                if (res && res == 204) {
                                    setOption('');
                                    fetchRoomData();
                                    toast.success('Member removed');
                                }
                            }}
                        >
                            <Button variant="danger">Remove</Button>
                        </div>
                    )}
                    {option == 'changeOwner' && (
                        <div className="button">
                            <Button
                                variant="info"
                                onClick={async () => {
                                    let res = await changeRoomOwner(roomId, userId);
                                    if (res && res == 204) {
                                        setOption('');
                                        fetchRoomData();
                                        toast.success('Change owner successfully');
                                    }
                                }}
                            >
                                Set as owner
                            </Button>
                        </div>
                    )}
                </>
            )}
        </div>
    );
}

export default MemberItem;
