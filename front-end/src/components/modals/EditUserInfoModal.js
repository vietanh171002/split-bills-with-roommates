import { useContext, useEffect, useState } from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';
import { UserContext } from '../../context/UserContext';
import { AppContext } from '../../context/AppContext';

export function EditUserInfoModal(props) {
    const { show, handleCloseModal, handleSubmitModal } = props;
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');

    const { user } = useContext(AppContext);

    useEffect(() => {
        setName(user.name);
        setEmail(user.email);
    }, [handleCloseModal]);

    return (
        <>
            <Modal show={show} onHide={handleCloseModal}>
                <Modal.Header closeButton>
                    <Modal.Title>Edit Info</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                            <Form.Label>Your name</Form.Label>
                            <Form.Control
                                type="text"
                                autoFocus
                                value={name}
                                onChange={(event) => setName(event.target.value)}
                            />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                            <Form.Label>Your email</Form.Label>
                            <Form.Control
                                type="email"
                                autoFocus
                                value={email}
                                onChange={(event) => setEmail(event.target.value)}
                            />
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleCloseModal}>
                        Close
                    </Button>

                    <Button variant="primary" onClick={() => handleSubmitModal(name, email)} disabled={!name || !email}>
                        Save changes
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}
