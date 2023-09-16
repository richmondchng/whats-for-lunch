export interface ResponseToken {
    data: TokenBody[]
}

interface TokenBody {
    accessToken: string;
}