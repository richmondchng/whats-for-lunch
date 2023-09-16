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

export {
    ResponseSessions, SessionBody, ResponseTokens, TokenBody, ResponseUsers, UserBody
}