import { useContext, useEffect, useState } from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';
import ReactDatePicker from 'react-datepicker';
import { DatePicker } from '@mui/x-date-pickers';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import dayjs from 'dayjs';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';

function InputExpenseModal(props) {
    const { addExpense, editExpense, show, handleCloseModal, handleSubmitInputExpenseModal } = props;
    const [expense, setExpense] = useState({ amount: '', category: '', date: new Date(), detail: '' });

    const categoryOptions = ['FOOD', 'GAS', 'INTERNET', 'BILL', 'OTHERS'];

    useEffect(() => {
        if (editExpense) {
            setExpense(editExpense);
            // setExpense((prevExpense) => ({ ...prevExpense, date: new Date(editExpense.date) }));
        }
    }, [editExpense]);

    const handleSubmit = () => handleSubmitInputExpenseModal(expense);

    return (
        <>
            <Modal show={show} onHide={handleCloseModal}>
                <Modal.Header closeButton>
                    {addExpense && <Modal.Title>Add Expense</Modal.Title>}
                    {editExpense && <Modal.Title>Edit Expense</Modal.Title>}
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                            <Form.Label>Amount</Form.Label>
                            <Form.Control
                                type="number"
                                placeholder="Enter amount"
                                defaultValue={editExpense && expense.amount ? expense.amount : ''}
                                autoFocus
                                onChange={(event) =>
                                    setExpense((prevExpense) => ({ ...prevExpense, amount: event.target.value }))
                                }
                            />
                        </Form.Group>
                        <Form.Group controlId="formCategory">
                            <Form.Label>Category</Form.Label>
                            <br />
                            {categoryOptions.map((category, index) => (
                                <Form.Check
                                    key={index}
                                    inline
                                    type="radio"
                                    defaultChecked={editExpense && expense.category === category}
                                    name="categoryRadio"
                                    label={category}
                                    onClick={(e) => {
                                        console.log(category);
                                        setExpense((prevExpense) => ({ ...prevExpense, category: category }));
                                    }}
                                />
                            ))}
                        </Form.Group>
                        <br />
                        <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                            <Form.Label>Payment date</Form.Label>

                            {/* <ReactDatePicker
                                // className="form-control"
                                selected={expense.date}
                                onChange={(date) => setExpense((prevExpense) => ({ ...prevExpense, date: date }))}
                            /> */}
                            <br />
                            <LocalizationProvider dateAdapter={AdapterDayjs}>
                                <DatePicker
                                    format="DD/MM/YYYY"
                                    defaultValue={editExpense && expense.date && dayjs(expense.date)}
                                    onChange={(date) => setExpense((prevExpense) => ({ ...prevExpense, date: date }))}
                                />
                            </LocalizationProvider>
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                            <Form.Label>Detail</Form.Label>
                            <Form.Control
                                as="textarea"
                                rows={3}
                                type="text"
                                defaultValue={editExpense && expense.detail ? expense.detail : ''}
                                onChange={(event) =>
                                    setExpense((prevExpense) => ({ ...prevExpense, detail: event.target.value }))
                                }
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
                        onClick={handleSubmit}
                        disabled={Object.getOwnPropertyNames(expense).length < 4}
                    >
                        {addExpense && 'Add expense'}
                        {editExpense && 'Save changes'}
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default InputExpenseModal;
