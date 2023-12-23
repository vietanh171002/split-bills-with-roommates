import { useContext, useEffect, useState } from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';

function InputTextModal(props) {
    const { createRoom, editRoomName, addMember, show, handleCloseModal, handleSubmitInputTextModal } = props;
    const [text, setText] = useState('');

    useEffect(() => {
        setText(editRoomName);
    }, [editRoomName]);
    useEffect(() => {
        if (!editRoomName) {
            setText('');
        }
    }, [handleCloseModal]);
    return (
        <>
            <Modal show={show} onHide={handleCloseModal}>
                <Modal.Header closeButton>
                    {createRoom && <Modal.Title>Create Room</Modal.Title>}
                    {editRoomName && <Modal.Title>Edit Room Name</Modal.Title>}
                    {addMember && <Modal.Title>Add Member To Room</Modal.Title>}
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                            {createRoom && <Form.Label>Room name</Form.Label>}
                            {editRoomName && <Form.Label>New name of the room</Form.Label>}
                            {addMember && <Form.Label>Input member email</Form.Label>}
                            <Form.Control
                                type={addMember ? 'text' : 'email'}
                                autoFocus
                                value={text}
                                onChange={(event) => setText(event.target.value)}
                            />
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleCloseModal}>
                        Close
                    </Button>

                    <Button variant="primary" onClick={() => handleSubmitInputTextModal(text)} disabled={!text}>
                        {createRoom && 'Create room'}
                        {editRoomName && 'Confirm'}
                        {addMember && 'Add'}
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default InputTextModal;
