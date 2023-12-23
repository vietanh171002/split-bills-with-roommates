import { useContext } from 'react';
import RoomInfoItem from '../components/RoomInfoItem/RoomInfoItem';
import SpendingItem from '../components/SpendingItem/SpendingItem';
import { RoomContext } from '../context/RoomContext';
import { UserContext } from '../context/UserContext';
import { AppContext } from '../context/AppContext';
function Spendings() {
    // const { listExpenses } = useContext(RoomContext);
    // const { user } = useContext(UserContext);
    // const { roomId, roomInfo } = useContext(RoomContext);
    const { listExpenses, user, roomId, roomInfo } = useContext(AppContext);

    return (
        <>
            {roomId && <RoomInfoItem expenses />}
            {listExpenses &&
                listExpenses.length > 0 &&
                listExpenses.map((expense, index) => (
                    <SpendingItem
                        isYourExpense={expense.spenderId === user.id}
                        isRoomOwner={user.id == roomInfo.ownerId}
                        currentExpense={expense}
                        key={index}
                        spender={expense.spender}
                        category={expense.category}
                        amount={expense.amount}
                        detail={expense.detail}
                        date={expense.date}
                        expenseId={expense.spendingId}
                    />
                ))}
        </>
    );
}

export default Spendings;
