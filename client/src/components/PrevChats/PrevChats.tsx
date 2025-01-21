//PrevChats.tsx: represents the previous chats section of the page 
import './PrevChats.css';

type PrevChatsProps = {
    prevChats: String[]
}
export default function PrevChats({prevChats}: PrevChatsProps) {
    return (
        <div className="prev-chats">
            {prevChats.map((chat, index) => (
                <p key={index}>{chat}</p>
            ))}
        </div>
    )
}

