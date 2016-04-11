var http = require('http');
var fs = require('fs');
var path = require('path');
var mime = require('mime');
var open = require('open');

var webRoot = './src/main/webapp/';
var protobufDir = webRoot+'protobuf/';
// Copy dependencies to '/protobuf' (example specific, you usually don't have to care
var deps = [
    [protobufDir+'long.js', './node_modules/protobufjs/node_modules/bytebuffer/node_modules/long/dist/long.js'],
    [protobufDir+'bytebuffer.js', './node_modules/protobufjs/node_modules/bytebuffer/dist/bytebuffer.js'],
    [protobufDir+'protobuf.js', './node_modules/protobufjs/dist/protobuf.js']
];
for (var i=0, dep; i<deps.length; i++) {
    dep = deps[i];
    if (!fs.existsSync(dep[0])) {
        console.log('Copying '+dep[0]+' from '+dep[1]);
        try {
            fs.writeFileSync(dep[0], fs.readFileSync(path.join(__dirname, dep[1])));
        } catch (err) {
            console.log('Copying failed: '+err.message);
            console.log('\nDid you run `npm install` ?');
            process.exit(1);
        }
    }
}

var ProtoBuf = require('protobufjs');
//console.log(ProtoBuf);
//console.log(__dirname);
var TestProtobuf = ProtoBuf.loadProtoFile(protobufDir+'TestProtobuf.proto').build('TestProtobuf'),
    TestProto = TestProtobuf.TestProto;
//console.log(TestProto);

var BufferHelper = require('bufferhelper');

var cache = {};
var port = 3000;

var server = http.createServer(function(request, response){
    var filePath = false;
    console.log('process '+request.url);
    if(request.url == '/'){
        filePath = 'index.html';
    }else if(request.method === 'POST' && request.url.lastIndexOf('.protobuf') != -1){
        //BufferHelper参考链接 http://www.infoq.com/cn/articles/nodejs-about-buffer/
        var bufferHelper = new BufferHelper();
        request.on('data', function (chunk) {
            bufferHelper.concat(chunk);
        });
        request.on('end', function () {
            var buffer = bufferHelper.toBuffer();
            var testProtoData = TestProto.decode(buffer);
            //console.log(testProtoData);
            response.writeHead(200, {'Content-Type': 'application/x-protobuf'});
            response.end(testProtoData.toBuffer());
        });

        return;
    }else{
        filePath = request.url;
    }

    var absPath = webRoot+filePath;
    serveStatic(response, cache, absPath);
});

server.listen(port, function(){
    console.log('Server listening on porn '+port);
    open('http://localhost:'+port);
});
server.on("error", function(err) {
    console.log("Failed to start server:", err);
    process.exit(1);
});

function serveStatic(response, cache, absPath){
    if(cache[absPath]){
        sendFile(response, absPath, cache[absPath]);
    }else{
        fs.exists(absPath, function(exists){
            if(!exists){
                send404(response);
                return;
            }

            fs.readFile(absPath, function(err, data){
                if(err){
                    send404(response);
                    return;
                }

                cache[absPath] = data;
                sendFile(response, absPath, data);
            });
        });
    }
}

function send404(response){
    response.writeHead(404, {'content-type':'text/plain'});
    response.write('Error 404: resource not found.');
    response.end();
}

function sendFile(response, filePath, fileContens){
    response.writeHead(200, {'content-type':mime.lookup(path.basename(filePath))});
    response.end(fileContens);
}