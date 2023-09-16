interface ResponseSessions {
    data: SessionBody[];
}

interface SessionBody {
    id: number;
    date: Date;
    owner: {
        id: number;
        userName: string;
        displayName: string;
    };
    participants: [{
        id: number;
        userName: string;
        displayName: string;
        status: string;
    }],
    restaurants: [{
        id: number;
        restaurantName: string;
        description: string;
        addedBy: number,
        status: string;
    }],
    selectedRestaurant?: string;
    status: string;
}

interface ResponseTokens {
    data: TokenBody[];
}

interface TokenBody {
    accessToken: string;
}

interface ResponseUsers {
    data: UserBody[];
}

interface UserBody {
    id: number;
    userName: string;
    firstName: string;
    lastName: string;
}

interface ResponseDeleteSession {
    data: DeleteSessionBody[]
}

interface DeleteSessionBody {
    sessionId: number;
    action: string;
    status: string;
}

export {
    ResponseSessions, SessionBody, ResponseTokens, TokenBody, ResponseUsers, UserBody,
    ResponseDeleteSession, DeleteSessionBody
}