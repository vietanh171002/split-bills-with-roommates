import { useContext, useState } from 'react';
import MemberItem from '../components/MemberItem/MemberItem';
import RoomInfoItem from '../components/RoomInfoItem/RoomInfoItem';
import { RoomContext } from '../context/RoomContext';
import Alert from 'react-bootstrap/Alert';
import { AppContext } from '../context/AppContext';

function Members() {
    const { roomId, listMembers } = useContext(AppContext);
    const [status, setStatus] = useState('');
    return (
        <>
            {roomId && (
                <RoomInfoItem
                    members
                    handleChangeOwner={() => setStatus('changeOwner')}
                    handleRemoveMember={() => setStatus('removeMember')}
                />
            )}
            {listMembers &&
                listMembers.length > 0 &&
                listMembers.map((member, index) => (
                    <MemberItem
                        status={status}
                        key={index}
                        userId={member.userId}
                        name={member.memberName}
                        balance={member.balance}
                        role={member.memberRole}
                        totalSpent={member.amountSpent}
                    />
                ))}
        </>
    );
}

export default Members;
