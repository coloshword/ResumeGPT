/** ChatInterface: defines the chat interface page */
import './ChatInteface.css';
import { useState } from 'react';
import ChatBox from '../../components/ChatBox/ChatBox';
import PrevChats from '../../components/PrevChats/PrevChats';

export default function ChatInterface() {
    // create the state to set and update chats, for the sake of this
    const [prevChats, setPrevChats] = useState<String[]>([])

    function addChat(message: String) {
        setPrevChats((prevChats) => [...prevChats, message])
    }

    return (
        <div className="chat-interface">
            <PrevChats
            prevChats={prevChats}/>
            <ChatBox
            onSendChat={addChat}/>
        </div>
    );
}