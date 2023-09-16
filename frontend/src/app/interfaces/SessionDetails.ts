export interface SessionDetails {
    id?: number;
    sessionDate: Date;
    ownerName: string;
    ownerId: number;
    status: string;
    selectedRestaurant?: string;
}