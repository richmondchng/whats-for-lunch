# stage 1 - build application in node
# https://nodejs.org/en/docs/guides/nodejs-docker-webapp
# https://typeofnan.dev/how-to-serve-a-react-app-with-nginx-in-docker/
FROM node:18 AS builder
# Create app directory
WORKDIR /usr/src/app
# copy package.json package-lock.json
COPY package*.json ./
# install dependencies
RUN npm install
# RUN npm ci --omit=dev
# bundle app source
COPY . .
# build dist
RUN npm run build

# stage 2 - nginx to host application
FROM nginx:1.25.1-alpine
# move to directory
WORKDIR /usr/share/nginx/html
# remove default nginx static assets
RUN rm -rf ./*
# copy from builder stage
COPY --from=builder /usr/src/app/dist/frontend .

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]