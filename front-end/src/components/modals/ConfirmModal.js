import { useContext, useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import { RoomContext } from '../../context/RoomContext';
import { AppContext } from '../../context/AppContext';

function ConfirmModal(props) {
    const { deleteRoom, deleteExpense, show, closeConfirmModal, handleConfirm } = props;
    const { roomInfo } = useContext(AppContext);
    const handleClose = () => closeConfirmModal();
    const handleClickConfirm = () => handleConfirm();

    return (
        <>
            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>{deleteRoom && `Delete Room: ${roomInfo.roomName}`} </Modal.Title>
                    <Modal.Title>{deleteExpense && `Delete Expense:`} </Modal.Title>
                    <Modal.Title>Delete </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {deleteRoom &&
                        'Deletion of room will trigger deletion of related data, are you sure to delete this rom'}
                    {deleteExpense && 'Are you sure to delete this expense'}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Close
                    </Button>
                    <Button variant="primary" onClick={handleClickConfirm}>
                        Confirm
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default ConfirmModal;
