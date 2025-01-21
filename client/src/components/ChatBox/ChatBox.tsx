// ChatBox.tsx: represents the chat box component 
import './ChatBox.css';
import sendChatIcon from "../../assets/send-chat.svg";

type ChatBoxProps = {
    onSendChat: (message: string ) => void
}
//ChatBox should t
export default function ChatBox({onSendChat}: ChatBoxProps) {
    function handleSendChat() {
        const chatInput = document.querySelector(".chat-box-input") as HTMLInputElement;
        if (chatInput && chatInput.value != '') {
            onSendChat(chatInput.value);
            chatInput.value = '';
        }
    }

    function handleEnterKey(e: React.KeyboardEvent<HTMLInputElement>) {
        if (e.key === 'Enter') {
            e.preventDefault();
            handleSendChat();
        }
    }

    return (
        <div className="chat-box">
            <input className="chat-box-input" type="text" placeholder="Type a message..." onKeyDown={handleEnterKey}></input>
            <div className="chat-box-action-bar">
                <button className="chat-box-send-chat-btn" onClick={handleSendChat}>
                    <img className="send-chat-icon" src={sendChatIcon}></img>
                </button>
            </div>
        </div>
    )
}