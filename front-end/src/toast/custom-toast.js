import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

toast.configure({
    autoClose: 1000,
    position: toast.POSITION.TOP_RIGHT,
});

export default toast;
