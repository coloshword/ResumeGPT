// ChatBox.tsx: represents the chat box component 
import './ChatBox.css';
import sendChatIcon from "../../assets/send-chat.svg";

type ChatBoxProps = {
    onSendChat: (message: string ) => void
}

enum MessageType {
    LLM,
    USER,
}
type Message = {
    text: String;
    type: MessageType
}

//ChatBox should t
export default function ChatBox({onSendChat}: ChatBoxProps) {

    async function postChat(message: Message) : Promise<any> {
        const endpoint: string = 'http://localhost:8080/post-chat';

        try {
            const response = await fetch(endpoint, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(message)
            });

            if (!response.ok) { 
                throw new Error(`Failed to post chat: ${response.status}`)
            }

            return await response.text();
        } catch (error) {
            console.error("Error:", error);
        }
    }

    function handleSendChat() {
        const chatInput = document.querySelector(".chat-box-input") as HTMLInputElement;
        if (chatInput && chatInput.value != '') {
            const chatInputVal = chatInput.value
            onSendChat(chatInputVal)
            chatInput.value = '';
            // also send chat to fetch
            const chatMsg: Message = {
                text: chatInputVal,
                type: MessageType.USER,
            }
            postChat(chatMsg).then((response: string) => {
                // add the response to the previousChats
                onSendChat(response)
            }).catch((error: Error) => {
                console.error(error)
            })
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