/** ChatInterface: defines the chat interface page */
import './ChatInteface.css';
import { useState } from 'react';
import ChatBox from '../../components/ChatBox/ChatBox';
import PrevChats from '../../components/PrevChats/PrevChats';
import { Message } from '../../components/ChatBox/ChatBox';

export default function ChatInterface() {
    // create the state to set and update chats, for the sake of this
    const [prevChats, setPrevChats] = useState<Message[]>([])

    function addChat(message: Message) {
        setPrevChats((prevChats) => [...prevChats, message])
    }

    return (
        <div className="chat-interface">
        <div className="prev-chats-scrollable">
            <PrevChats prevChats={prevChats} />
        </div>
        <ChatBox onSendChat={addChat} />
        </div>
    );
}