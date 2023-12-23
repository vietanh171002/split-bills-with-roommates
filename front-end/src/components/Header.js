import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import logo from '../assets/images/logo512.png';
import { NavLink, useLocation, useNavigate } from 'react-router-dom';
import InputTextModal from './modals/InputTextModal';
import { useContext, useState } from 'react';
import { toast } from 'react-toastify';
import { createRoom } from '../api/RoomApi';
import ChangePasswordModal from './modals/ChangePasswordModal';
import { changePassword, editUserInfo } from '../api/UserApi';
import { EditUserInfoModal } from './modals/EditUserInfoModal';
import { AppContext } from '../context/AppContext';

function Header() {
    const location = useLocation();

    //get from contexts
    const { user, listRooms, clearUserContext, fetchUserData, roomId, setContextRoomId, fetchRoomData } =
        useContext(AppContext);

    //modal create new room
    const [showCreateNewRoomModal, setShowCreateNewRoomModal] = useState(false);

    //modal change pass
    const [showChangePasswordModal, setShowChangePasswordModal] = useState(false);

    //modal edit info
    const [showEditUserInfoModal, setShowEditUserInfoModal] = useState(false);

    let navigate = useNavigate();

    return (
        <>
            <Navbar expand="lg" className="bg-body-tertiary">
                <Container>
                    <Navbar.Brand id="basic-navbar-nav">
                        <NavLink className="navbar-brand" to="/">
                            <img src={logo} alt="" width="30" height="30" className="d-inline-block align-top mx-2" />
                            <span>SPLIT BILLS WITH ROOMMATES</span>
                        </NavLink>
                    </Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav" />
                    <Navbar.Collapse id="basic-navbar-nav">
                        {user && (
                            <>
                                <Nav className="me-auto">
                                    <NavLink className="nav-link" to="/expenses">
                                        Expenses
                                    </NavLink>
                                    <NavLink className="nav-link" to="/members">
                                        Members
                                    </NavLink>

                                    <NavDropdown title="Your rooms" id="basic-nav-dropdown">
                                        {listRooms && listRooms.length > 0 ? (
                                            listRooms.map((room, index) => (
                                                <NavDropdown.Item
                                                    key={index}
                                                    onClick={() => {
                                                        setContextRoomId(room.roomId);
                                                    }}
                                                >
                                                    {room.roomName}
                                                </NavDropdown.Item>
                                            ))
                                        ) : (
                                            <NavDropdown.Item disabled>No room available</NavDropdown.Item>
                                        )}
                                        <NavDropdown.Divider />
                                        <NavDropdown.Item onClick={() => setShowCreateNewRoomModal(true)}>
                                            Create new room
                                        </NavDropdown.Item>
                                    </NavDropdown>
                                </Nav>

                                <Nav className="me-auto">
                                    <strong>Welcome {user.name} !</strong>
                                </Nav>
                                <Nav className="me-auto">
                                    <NavDropdown id="basic-nav-dropdown">
                                        <NavDropdown.Item onClick={() => setShowEditUserInfoModal(true)}>
                                            Edit info
                                        </NavDropdown.Item>
                                        <NavDropdown.Item onClick={() => setShowChangePasswordModal(true)}>
                                            Reset password
                                        </NavDropdown.Item>
                                        <NavDropdown.Item onClick={clearUserContext}>Logout</NavDropdown.Item>
                                    </NavDropdown>
                                </Nav>
                            </>
                        )}
                        {!user && (
                            <>
                                <Nav className="me-auto" />
                                <Nav className="me-auto" />
                                <Nav className="me-auto" />
                                <Nav className="me-auto" activeKey={location.pathname}>
                                    <NavLink to="/login" className="nav-link">
                                        Login
                                    </NavLink>

                                    <NavLink to="/register" className="nav-link">
                                        Register
                                    </NavLink>
                                </Nav>
                            </>
                        )}
                    </Navbar.Collapse>
                </Container>
            </Navbar>
            <InputTextModal
                createRoom
                show={showCreateNewRoomModal}
                handleCloseModal={() => setShowCreateNewRoomModal(false)}
                handleSubmitInputTextModal={async (roomName) => {
                    let res = await createRoom(roomName);
                    if (res && res.roomId) {
                        toast.success('Create new room successfully');
                        setContextRoomId(res.roomId);
                        navigate('/members');
                        setShowCreateNewRoomModal(false);
                        fetchUserData();
                    }
                }}
            />
            <ChangePasswordModal
                show={showChangePasswordModal}
                handleCloseModal={() => {
                    setShowChangePasswordModal(false);
                }}
                handleSubmitModal={async (oldPassword, newPassword) => {
                    let res = await changePassword(oldPassword, newPassword);
                    if (res && res == 204) {
                        setShowChangePasswordModal(false);
                        toast.success('Password changed');
                    }
                }}
            />
            <EditUserInfoModal
                show={showEditUserInfoModal}
                handleCloseModal={() => {
                    setShowEditUserInfoModal(false);
                }}
                handleSubmitModal={async (name, email) => {
                    let res = await editUserInfo(name, email);
                    if (res && res == 204) {
                        setShowEditUserInfoModal(false);
                        fetchUserData();
                        if (roomId) {
                            fetchRoomData();
                        }
                        toast.success('Edit info successfully');
                    }
                    if (res && res == 'ok') {
                        setShowEditUserInfoModal(false);
                        toast.success('Edit info successfully, please login again with new email');
                        clearUserContext();
                    }
                }}
            />
        </>
    );
}

export default Header;
