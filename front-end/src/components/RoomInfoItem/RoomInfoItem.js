import './RoomInfoItem.scss';
import Button from 'react-bootstrap/Button';
import DropdownButton from 'react-bootstrap/DropdownButton';
import Dropdown from 'react-bootstrap/Dropdown';
import { useContext, useState } from 'react';
import { RoomContext } from '../../context/RoomContext';
import InputTextModal from '../modals/InputTextModal';
import { addUserToRoom, deleteRoom, editRoomName, leaveRoom } from '../../api/RoomApi';
import { UserContext } from '../../context/UserContext';
import ConfirmModal from '../modals/ConfirmModal';
import { toast } from 'react-toastify';
import { useNavigate } from 'react-router-dom';
import InputExpenseModal from '../modals/InputExpenseModal';
import { addExpense } from '../../api/ExpenseApi';
import { AppContext } from '../../context/AppContext';

export default function RoomInfoItem(props) {
    const { expenses, members, handleChangeOwner, handleRemoveMember } = props;
    const { roomInfo, isRoomOwner, roomId, fetchRoomData, clearRoomContext } = useContext(AppContext);
    const { user, fetchUserData } = useContext(AppContext);

    //logged user is room owner or not?
    // const isRoomOwner = roomInfo.ownerId === user.id;
    let navigate = useNavigate();

    //edit room name
    const [showModalEditRoomName, setShowModalEditRoomName] = useState(false);

    //delete room
    const [showDeleteRoomModal, setShowDeleteRoomModal] = useState(false);

    //add member
    const [showAddMemberModal, setShowAddMemberModal] = useState(false);

    //add expense
    const [showAddExpenseModal, setShowAddExpenseModal] = useState(false);

    return (
        <div className="room-info-item">
            <div className="icon">
                <i className="fa-solid fa-house"></i>
            </div>

            <div className="info">
                <div className="room-name">
                    <h2>{roomInfo.roomName}</h2>
                </div>
                <div className="owner-info">
                    {isRoomOwner ? (
                        <>
                            <span className="owner-name" style={{ color: '#157347' }}>
                                <strong> You are room owner</strong>
                            </span>
                        </>
                    ) : (
                        <>
                            <span className="owner">Owner:</span>
                            <span className="owner-name">
                                <strong>{roomInfo.owner} </strong>
                            </span>
                        </>
                    )}
                </div>
                <div className="owner-info">
                    <span className="owner">Number of members:</span>
                    <span className="owner-name">
                        <strong>{roomInfo.memberCount}</strong>
                    </span>
                </div>
                <div className="total-info">
                    <span className="total">Total amount:</span>
                    <div className="amount">
                        <strong>
                            <span className="amount">{roomInfo.totalAmount} </span>
                            <span className="usd">USD</span>
                        </strong>
                    </div>
                </div>
            </div>

            <div className="button">
                {expenses && (
                    <>
                        <Button className="add-spending" variant="success" onClick={() => setShowAddExpenseModal(true)}>
                            Add expense
                        </Button>

                        <DropdownButton title="" id="bg-nested-dropdown">
                            <Dropdown.Item eventKey="1" onClick={() => setShowAddMemberModal(true)}>
                                Add member
                            </Dropdown.Item>
                        </DropdownButton>
                    </>
                )}
                {members && (
                    <>
                        <Button className="add-spending" variant="success" onClick={() => setShowAddMemberModal(true)}>
                            Add member
                        </Button>

                        <DropdownButton title="" id="bg-nested-dropdown">
                            {isRoomOwner && (
                                <>
                                    <Dropdown.Item eventKey="1" onClick={() => setShowModalEditRoomName(true)}>
                                        Change room name
                                    </Dropdown.Item>
                                    <Dropdown.Item eventKey="1" onClick={handleRemoveMember}>
                                        Remove member
                                    </Dropdown.Item>
                                    <Dropdown.Item eventKey="1" onClick={handleChangeOwner}>
                                        Change owner
                                    </Dropdown.Item>
                                    <Dropdown.Item eventKey="3" onClick={() => setShowDeleteRoomModal(true)}>
                                        Delete room
                                    </Dropdown.Item>
                                </>
                            )}
                            {!isRoomOwner && (
                                <Dropdown.Item
                                    eventKey="2"
                                    onClick={async () => {
                                        let res = await leaveRoom(roomId);
                                        if (res && res == 204) {
                                            fetchUserData();
                                            // fetchRoomData();
                                            clearRoomContext();
                                            toast('You have left the room');
                                            navigate('/');
                                        }
                                    }}
                                >
                                    Leave room
                                </Dropdown.Item>
                            )}
                        </DropdownButton>
                    </>
                )}
            </div>

            <InputTextModal
                editRoomName={roomInfo.roomName}
                show={showModalEditRoomName}
                handleCloseModal={() => setShowModalEditRoomName(false)}
                handleSubmitInputTextModal={async (newRoomName) => {
                    let res = await editRoomName(roomId, newRoomName);
                    if (res && res == 204) {
                        fetchUserData();
                        fetchRoomData();
                        setShowModalEditRoomName(false);
                    }
                }}
            />
            <InputTextModal
                addMember
                show={showAddMemberModal}
                handleCloseModal={() => setShowAddMemberModal(false)}
                handleSubmitInputTextModal={async (addedUserEmail) => {
                    let res = await addUserToRoom(roomId, addedUserEmail);
                    if (res && res == 204) {
                        toast.success('Add member successfully');
                        fetchRoomData();
                        setShowAddMemberModal(false);
                    }
                }}
            />
            <ConfirmModal
                deleteRoom
                show={showDeleteRoomModal}
                closeConfirmModal={() => setShowDeleteRoomModal(false)}
                handleConfirm={async () => {
                    let res = await deleteRoom(roomId);
                    if (res && res == 204) {
                        toast.success('Delete room successfully');
                        clearRoomContext();
                        fetchUserData();
                        setShowDeleteRoomModal(false);
                        navigate('/');
                    }
                }}
            />
            <InputExpenseModal
                addExpense
                show={showAddExpenseModal}
                handleCloseModal={() => setShowAddExpenseModal(false)}
                handleSubmitInputExpenseModal={async (expense) => {
                    let res = await addExpense(roomId, expense);
                    if (res && res == 204) {
                        fetchRoomData();
                        setShowAddExpenseModal(false);
                    }
                }}
            />
        </div>
    );
}
