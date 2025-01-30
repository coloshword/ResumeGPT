//PrevChats.tsx: represents the previous chats section of the page 
import './PrevChats.css';
import { Message, MessageType } from '../ChatBox/ChatBox';

type PrevChatsProps = {
    prevChats: Message[]
}
export default function PrevChats({prevChats}: PrevChatsProps) {
    return (
        <div className="prev-chats">
            {prevChats.map((chat, index) => (
                <div className={chat.type == MessageType.USER ? "user-chat" : "LLM-chat"}>
                    <p key={index}>{chat.text}</p>
                </div>
            ))}
        </div>
    )
}

