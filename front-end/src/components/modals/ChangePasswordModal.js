import { useContext, useEffect, useState } from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';

function ChangePasswordModal(props) {
    const { show, handleCloseModal, handleSubmitModal } = props;
    const [oldPassword, setOldPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');

    useEffect(() => {
        setNewPassword('');
        setOldPassword('');
    }, [handleCloseModal]);

    return (
        <>
            <Modal show={show} onHide={handleCloseModal}>
                <Modal.Header closeButton>
                    <Modal.Title>Change Password</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                            <Form.Label>Current password</Form.Label>
                            <Form.Control
                                type="password"
                                autoFocus
                                value={oldPassword}
                                onChange={(event) => setOldPassword(event.target.value)}
                            />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                            <Form.Label>New password</Form.Label>
                            <Form.Control
                                type="password"
                                autoFocus
                                value={newPassword}
                                onChange={(event) => setNewPassword(event.target.value)}
                            />
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleCloseModal}>
                        Close
                    </Button>

                    <Button
                        variant="primary"
                        onClick={() => handleSubmitModal(oldPassword, newPassword)}
                        disabled={!newPassword || !oldPassword}
                    >
                        Confirm
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default ChangePasswordModal;
