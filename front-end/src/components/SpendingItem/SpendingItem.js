import './SpedingItem.scss';
import Button from 'react-bootstrap/Button';
// import InputExpenseModal from '../components/modals/InputExpenseModal';
import InputExpenseModal from '../modals/InputExpenseModal';
import { useContext, useEffect, useState } from 'react';
import { deleteExpense, editExpense } from '../../api/ExpenseApi';
import { RoomContext } from '../../context/RoomContext';
import { toast } from 'react-toastify';
import ConfirmModal from '../modals/ConfirmModal';
import moment from 'moment';
import { AppContext } from '../../context/AppContext';

function SpendingItem(props) {
    const { expenseId, spender, category, amount, detail, date, isYourExpense, currentExpense } = props;
    const [showEditExpenseModal, setShowEditExpenseModal] = useState(false);
    const [showDeleteExpenseModal, setShowDeleteExpenseModal] = useState(true);
    const { fetchRoomData, isRoomOwner } = useContext(AppContext);

    return (
        <>
            <div className="spending-item">
                <div className="icon">
                    <i className="fa-solid fa-money-check-dollar"></i>
                    {isYourExpense && <p>Your expense</p>}
                </div>

                <div className="info">
                    <table>
                        <tbody>
                            <tr>
                                <td className="col-1">
                                    <strong className="spender">{spender}</strong>
                                </td>
                                <td className="col-2">
                                    <strong>
                                        <span className="amount">{amount}</span>
                                        <span>USD</span>
                                    </strong>
                                </td>
                            </tr>
                            <tr>
                                <td className="col-1">
                                    <span className="category">Category:</span>
                                    {category}
                                </td>

                                <td className="col-2">
                                    <strong>Detail:</strong> {detail}
                                </td>
                            </tr>
                            <tr>
                                <td className="col-1">
                                    <span className="category">Payment date:</span>
                                    {moment(date).format('DD/MM/YYYY')}
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <div className="button">
                    {isYourExpense && (
                        <>
                            <Button className="edit" variant="warning" onClick={() => setShowEditExpenseModal(true)}>
                                Edit
                            </Button>
                        </>
                    )}
                    {(() => {
                        if (isRoomOwner || isYourExpense) {
                            return (
                                <Button
                                    variant="danger"
                                    onClick={async () => {
                                        console.log('exId>> ', expenseId);
                                        let res = await deleteExpense(expenseId);
                                        if (res && res == 204) {
                                            fetchRoomData();
                                            toast('Expense deleted');
                                        }
                                    }}
                                >
                                    Delete
                                </Button>
                            );
                        }
                    })()}
                </div>
            </div>
            <InputExpenseModal
                editExpense={currentExpense}
                show={showEditExpenseModal}
                handleCloseModal={() => setShowEditExpenseModal(false)}
                handleSubmitInputExpenseModal={async (expense) => {
                    let res = await editExpense(currentExpense.spendingId, expense);
                    if (res && res == 204) {
                        fetchRoomData();
                        setShowEditExpenseModal(false);
                        toast.success('Edit expense successfully');
                    }
                }}
            />
            {showDeleteExpenseModal && (
                <ConfirmModal
                    deleteExpense
                    closeConfirmModal={() => setsetShowDeleteExpenseModal(true)}
                    handleConfirm={async () => {
                        let res = await deleteExpense(currentExpense.spendingId);
                        if (res) {
                            console.log('res...', res);
                        }
                        if (res && res == 204) {
                            fetchRoomData();
                            setShowDeleteExpenseModal(false);
                            toast.success('Delete expense successfully');
                        }
                    }}
                />
            )}
        </>
    );
}

export default SpendingItem;
