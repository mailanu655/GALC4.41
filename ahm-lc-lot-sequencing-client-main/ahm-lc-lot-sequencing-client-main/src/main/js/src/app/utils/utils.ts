export const getRandomColor = (str: string) => {
  let hash = 0;
  for (let i = 0; i < str?.length; i++) {
    hash = str.charCodeAt(i) + ((hash << 5) - hash);
  }

  const rgb = [0, 0, 0];
  for (let i = 0; i < 3; i++) {
    const value = (hash >> (i * 8)) & 255;
    rgb[i] = value;
  }

  return `rgb(${rgb[0]}, ${rgb[1]}, ${rgb[2]}, 0.3)`;
};

export const isJson = (str: string) => {
  try {
    let o = JSON.parse(str);
    return (o && typeof o === 'object' && o.constructor === Object) || Array.isArray(o);
  } catch (e) {
    return false;
  }
};

export const copyToClipboard = (text: string) => {
  navigator.clipboard.writeText(text).then(function() {
      console.log('Copying to clipboard was successful!');
  }, function(err) {
      console.error('Could not copy text: ', err);
  });
}

export const getDecimalPart = (n: number): string => {
  const decimalPart = (n + '').split('.')[1];
  return decimalPart;
}

export const getDateString = (date: Date) => {
  const month = (date.getMonth() + 1).toString().padStart(2, '0'); // getMonth() returns a 0-based value, hence we add 1
  const day = date.getDate().toString().padStart(2, '0');
  const year = date.getFullYear();
  const formattedDate = year + month + day;

  return formattedDate;
}