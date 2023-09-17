interface ResponseSessions {
    data: SessionBody[];
}

interface SessionBody {
    id: number;
    date: Date;
    owner: OwnerBody;
    participants: ParticipantBody[],
    restaurants: RestaurantBody[],
    selectedRestaurant?: number;
    status: string;
}

interface OwnerBody {
    id: number;
    userName: string;
    displayName: string;
}

interface ParticipantBody {
    id: number;
    userName: string;
    displayName: string;
    status: string;
}
interface RestaurantBody {
    id: number;
    restaurantName: string;
    description: string;
    addedBy: number,
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
    data: [{
        sessionId: number;
        action: string;
        status: string;
    }]
}

interface ResponseAddRestaurant {
    data: [{
        status: string;
    }]
}

interface ResponseSelectRestaurant {
    data: [{
        sessionId: number;
        restaurantId: number;
        restaurantName: string;
    }]
}

export {
    ResponseSessions, SessionBody, OwnerBody, ParticipantBody, RestaurantBody,
    ResponseTokens, TokenBody, ResponseUsers, UserBody,
    ResponseDeleteSession, ResponseAddRestaurant, ResponseSelectRestaurant
}