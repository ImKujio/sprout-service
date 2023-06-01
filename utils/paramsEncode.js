function paramsEncode(obj, prefix = null) {
    const str = [];
    for (let key in obj) {
        if (obj.hasOwnProperty(key)) {
            const value = obj[key];
            const finalKey = prefix ? `${prefix}[${key}]` : key;
            if (value !== null && Array.isArray(value)) {
                const encodedKey = encodeURIComponent(finalKey);
                const encodedValues = value.map((val) => encodeURIComponent(val)).join(",");
                str.push(`${encodedKey}=${encodedValues}`);
            } else if (value !== null && typeof value === "object") {
                str.push(paramsEncode(value, finalKey));
            } else if (value !== undefined) {
                str.push(`${encodeURIComponent(finalKey)}=${encodeURIComponent(value)}`);
            }
        }
    }
    return str.join("&");
}

const testStudentList = {
    "where": [
        "name|like|小",
        "age|in|12|13|14",
        "schoolDismissalTime|between|17:00|17:30"
    ],
    "order": [
        "name|desc",
        "age"
    ],
    "page": [20, 1]
}

const sysUserAll = {
    "fields": ["name","nickName"]
}

const params = paramsEncode(sysUserAll)
console.log("?" + params);


